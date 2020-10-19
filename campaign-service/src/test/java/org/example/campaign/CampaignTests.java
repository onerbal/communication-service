package org.example.campaign;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@ActiveProfiles("integration")
@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = CampaignTests.Initializer.class)
public class CampaignTests {

    private static final int REDIS_PORT = 6379;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${campaign.stream.key}")
    private String campaignKey;

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
        }
    }

    @Before
    public void init() {

    }

    @Test
    public void testCampaignKeyExist() {
        assertTrue(redisTemplate.hasKey(campaignKey));
    }
}
