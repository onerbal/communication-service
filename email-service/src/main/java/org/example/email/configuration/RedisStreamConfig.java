package org.example.email.configuration;

import org.example.common.model.notification.email.EmailNotificationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;
import org.springframework.data.redis.stream.Subscription;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
public class RedisStreamConfig {

    @Value("${email.notification.stream.key}")
    private String emailNotificationStreamKey;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StreamListener<String, ObjectRecord<String, EmailNotificationModel>> emailNotificationListener;

    @Bean
    public Subscription emailNotificationSubscription(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {

        StreamMessageListenerContainer.StreamMessageListenerContainerOptions options = StreamMessageListenerContainer.StreamMessageListenerContainerOptions
                .builder()
                .pollTimeout(Duration.ZERO)
                .targetType(EmailNotificationModel.class)
                .executor(messageExecutor())
                .build();

        StreamMessageListenerContainer container = StreamMessageListenerContainer.create(redisConnectionFactory, options);

        StreamOffset<String> offset = StreamOffset.create(emailNotificationStreamKey, ReadOffset.lastConsumed());

        Consumer consumer = Consumer.from(emailNotificationStreamKey, InetAddress.getLocalHost().getHostName());

        StreamMessageListenerContainer.StreamReadRequest<String> streamReadRequest = StreamMessageListenerContainer.StreamReadRequest.builder(offset)
                .errorHandler(throwable -> System.out.println("Error: " + throwable.getMessage()))
                .cancelOnError(e -> false)
                .consumer(consumer)
                .autoAcknowledge(false)
                .build();

        Subscription subscription =  container.register(streamReadRequest, emailNotificationListener);

        container.start();

        return subscription;
    }

    @PostConstruct
    void init() {
        try {
            if(!redisTemplate.hasKey(emailNotificationStreamKey))
                redisTemplate.opsForStream().createGroup(emailNotificationStreamKey, emailNotificationStreamKey);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public SimpleAsyncTaskExecutor messageExecutor() {

        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();

        executor.setThreadNamePrefix("emailNotificationStreamTask-");
        executor.setConcurrencyLimit(10);

        return executor;
    }
}
