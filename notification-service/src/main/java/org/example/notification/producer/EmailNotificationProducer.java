package org.example.notification.producer;

import org.example.common.model.notification.email.EmailNotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationProducer {

    @Value("${email.notification.stream.key}")
    private String emailNotificationStreamKey;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;

    public void publishEvent(EmailNotificationModel emailNotification) {
        ObjectRecord<String, EmailNotificationModel> emailNotificationRecord = StreamRecords.newRecord()
                .ofObject(emailNotification)
                .withStreamKey(emailNotificationStreamKey);

        redisTemplate.opsForStream()
                .add(emailNotificationRecord)
                .subscribe();
    }
}
