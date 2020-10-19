package org.example.notification.consumer;

import org.example.common.event.UserCampaignNotificationEvent;
import org.example.common.model.campaign.CampaignModel;
import org.example.common.model.notification.email.EmailNotificationModel;
import org.example.common.model.notification.email.EmailNotificationTemplate;
import org.example.common.model.user.UserModel;
import org.example.common.utils.StringUtils;
import org.example.notification.producer.EmailNotificationProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationConsumer implements StreamListener<String, ObjectRecord<String, UserCampaignNotificationEvent>>  {

    @Autowired
    private EmailNotificationProducer emailNotificationProducer;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${notification.stream.key}")
    private String notificationStreamKey;


    @Override
    public void onMessage(ObjectRecord<String, UserCampaignNotificationEvent> record) {
        UserCampaignNotificationEvent userCampaignNotificationEvent = record.getValue();
        UserModel user = userCampaignNotificationEvent.getUser();
        CampaignModel campaign = userCampaignNotificationEvent.getCampaign();
        if(null != user && null != campaign) {
            if(StringUtils.isNotBlank(user.getEmail())) {
                EmailNotificationModel emailNotificationModel = EmailNotificationModel
                        .builder()
                        .subject("New Campaign")
                        .email(user.getEmail())
                        .template(EmailNotificationTemplate.NEW_CAMPAIGN)
                        .build();

                emailNotificationModel.setContent(campaign.getName());
                emailNotificationProducer.publishEvent(emailNotificationModel);
            }

            if(StringUtils.isNotBlank(user.getMobile())) {
                // Send SMS Notification
            }

            if(StringUtils.isNotBlank(user.getDeviceId())) {
                // Send Push Notification
            }
        }

        redisTemplate.opsForStream().acknowledge(notificationStreamKey, record);
        redisTemplate.opsForStream().delete(record);
    }
}
