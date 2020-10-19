package org.example.campaign.service.impl;

import org.example.campaign.model.Campaign;
import org.example.campaign.producer.CampaignProducer;
import org.example.campaign.repository.CampaignRepository;
import org.example.campaign.service.CampaignService;
import org.example.common.converter.ModelConverter;
import org.example.common.event.CampaignCreatedEvent;
import org.example.common.model.campaign.CampaignModel;
import org.example.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
public class CampaignServiceImpl implements CampaignService {

    @Autowired
    private CampaignRepository campaignRepository;

    @Autowired
    private CampaignProducer campaignProducer;

    @Autowired
    private ApplicationContext applicationContext;

    private CampaignService self;

    @PostConstruct
    void init() {
        self = applicationContext.getBean(CampaignServiceImpl.class);
    }

    @Override
    public Campaign createCampaign() {
        Campaign campaign = campaignRepository.save(Campaign.builder().name("Campaign " + StringUtils.generateRandomString(5)).build());
        campaignProducer.publishEvent(CampaignCreatedEvent.builder().campaign((CampaignModel) ModelConverter.convert(campaign, CampaignModel.class)).build());
        return campaign;
    }
}
