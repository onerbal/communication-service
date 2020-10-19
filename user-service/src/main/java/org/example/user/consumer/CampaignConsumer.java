package org.example.user.consumer;

import org.example.common.converter.ModelConverter;
import org.example.common.event.CampaignCreatedEvent;
import org.example.common.event.UserCampaignNotificationEvent;
import org.example.common.model.campaign.CampaignModel;
import org.example.common.model.user.UserModel;
import org.example.common.utils.CollectionUtils;
import org.example.user.model.User;
import org.example.user.producer.UserNotificationProducer;
import org.example.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampaignConsumer implements StreamListener<String, ObjectRecord<String, CampaignCreatedEvent>> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${campaign.stream.key}")
    private String campaignStreamKey;

    @Autowired
    private UserNotificationProducer notifyUserEventProducer;

    @Autowired
    private UserService userService;

    @Override
    public void onMessage(ObjectRecord<String, CampaignCreatedEvent> record) {
        CampaignCreatedEvent event = record.getValue();
        CampaignModel campaign = event.getCampaign();
        System.out.println("Received new campaign, id: " + campaign.getId() + " name: " + campaign.getName());

        List<User> userList = userService.findAll();
        if(CollectionUtils.isNotEmpty(userList)) {
            userList.forEach(user -> notifyUserEventProducer.publishEvent(UserCampaignNotificationEvent
                    .builder()
                    .campaign(campaign)
                    .user((UserModel) ModelConverter.convert(user, UserModel.class))
                    .build()
            ));

            redisTemplate.opsForStream().acknowledge(campaignStreamKey, record);
            redisTemplate.opsForStream().delete(record);
        }
    }
}