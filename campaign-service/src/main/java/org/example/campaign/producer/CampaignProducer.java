package org.example.campaign.producer;

import org.example.common.event.CampaignCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CampaignProducer {

    @Value("${campaign.stream.key}")
    private String campaignStreamKey;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public void publishEvent(CampaignCreatedEvent event) {
        ObjectRecord<String, CampaignCreatedEvent> campaignEvent = StreamRecords.newRecord()
                .ofObject(event)
                .withStreamKey(campaignStreamKey);

        redisTemplate.opsForStream()
                .add(campaignEvent)
                .subscribe();
    }
}
