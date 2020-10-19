package org.example.campaign.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.PostConstruct;

@Configuration
public class RedisStreamConfig {

    @Value("${campaign.stream.key}")
    private String campaignStreamKey;

    @Autowired
    @Qualifier("customRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    @PostConstruct
    void init() {
        try {
            if(!redisTemplate.hasKey(campaignStreamKey))
                redisTemplate.opsForStream().createGroup(campaignStreamKey, campaignStreamKey);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
