package org.example.user.producer;

import org.example.common.event.UserCampaignNotificationEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserNotificationProducer {

    @Value("${notification.stream.key}")
    private String notificationStreamKey;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public void publishEvent(UserCampaignNotificationEvent userCampaignNotification) {
        ObjectRecord<String, UserCampaignNotificationEvent> campaignNotificationRecord = StreamRecords.newRecord()
                .ofObject(userCampaignNotification)
                .withStreamKey(notificationStreamKey);

        redisTemplate.opsForStream()
                .add(campaignNotificationRecord)
                .subscribe();
    }
}

