package org.example.user;

import org.example.user.model.User;
import org.example.user.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.images.PullPolicy;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ActiveProfiles("integration")
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = UserTests.Initializer.class)
public class UserTests {

    private static final int REDIS_PORT = 6379;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${notification.stream.key}")
    private String notificationKey;

    @Value("${campaign.stream.key}")
    private String campaignKey;


    @Container
    public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer()
            .withPassword("inmemory")
            .withUsername("inmemory");

    @Container
    public static GenericContainer campaignServiceContainer = new GenericContainer("campaign-service:latest")
            .withImagePullPolicy(PullPolicy.defaultPolicy()).withExposedPorts(8080);

    protected void cleanCache() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static GenericContainer redis = new GenericContainer<>("redis:6-alpine")
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            redis.start();
            String redisContainerIP = "spring.redis.host=" + redis.getContainerIpAddress();
            String redisContainerPort = "spring.redis.port=" + redis.getMappedPort(REDIS_PORT);
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, redisContainerIP, redisContainerPort);
            campaignServiceContainer.start();
        }
    }

    @Before
    public void init() {

    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public TestRestTemplate testRestTemplate;

    @DynamicPropertySource
    static void postgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @Test
    public void testInitialUserCount() {
        assertEquals(100000, userRepository.findAll().size());
    }

    @Test
    public void testGetUser() {

        ResponseEntity<User> response = testRestTemplate.getForEntity("/api/v1/user/1", User.class);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getUsername());
    }

    @Test
    public void testCampaignKeyExist() {
        assertTrue(redisTemplate.hasKey(campaignKey));
    }

    @Test
    public void testNotificationKeyExist() {
        assertTrue(redisTemplate.hasKey(notificationKey));
    }
}