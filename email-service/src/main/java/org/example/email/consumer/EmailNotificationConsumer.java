package org.example.email.consumer;

import org.example.common.model.notification.email.EmailNotificationModel;
import org.example.email.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class EmailNotificationConsumer implements StreamListener<String, ObjectRecord<String, EmailNotificationModel>> {

    @Autowired
    private EmailService emailService;

    @Value("${email.notification.stream.key}")
    private String emailNotificationStreamKey;

    ExecutorService executor = Executors.newFixedThreadPool(10);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(ObjectRecord<String, EmailNotificationModel> record) {
        EmailNotificationModel notification = record.getValue();

        executor.submit(() -> {
            emailService.sendEmail(notification.getEmail(), notification.getContent(), notification.getContent());
            redisTemplate.opsForStream().acknowledge(emailNotificationStreamKey, record);
            redisTemplate.opsForStream().delete(record);
        });
    }

    @PreDestroy
    void destroy() {
        executor.shutdown();
    }
}
