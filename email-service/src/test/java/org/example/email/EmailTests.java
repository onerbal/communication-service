package org.example.email;

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
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ActiveProfiles("integration")
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = EmailTests.Initializer.class)
public class EmailTests {

    private static final int REDIS_PORT = 6379;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public TestRestTemplate testRestTemplate;

    @Value("${email.notification.stream.key}")
    private String emailNotificationStreamKey;

    protected void cleanCache() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static GenericContainer redis = new GenericContainer<>("redis:6-alpine")
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);

        static GenericContainer inbucketContainer = new GenericContainer("inbucket/inbucket:latest")
                .withExposedPorts(2500)
                .withReuse(true);

        @Override
        public void initialize(ConfigurableApplicationContext context) {
            redis.start();
            inbucketContainer.start();
            String redisContainerIP = "spring.redis.host=" + redis.getContainerIpAddress();
            String redisContainerPort = "spring.redis.port=" + redis.getMappedPort(REDIS_PORT);

            String inbucketContainerIP = "spring.mail.host=" + inbucketContainer.getContainerIpAddress();
            String inbucketContainerPort = "spring.mail.port=" + inbucketContainer.getMappedPort(2500);

            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(context, redisContainerIP, redisContainerPort,
                    inbucketContainerIP, inbucketContainerPort);
        }
    }

    @Before
    public void init() {

    }

    @Test
    public void testSendEmail() {

        ResponseEntity<Boolean> response = testRestTemplate.postForEntity("/api/v1/email/send", null, Boolean.class);

        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertNotNull(response.getBody());
        assertTrue(response.getBody());
    }

    @Test
    public void testEmailNotificationKeyExist() {
        assertTrue(redisTemplate.hasKey(emailNotificationStreamKey));
    }
}
