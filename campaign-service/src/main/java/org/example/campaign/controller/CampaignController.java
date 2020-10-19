package org.example.campaign.controller;

import org.example.campaign.service.CampaignService;
import org.example.common.converter.ModelConverter;
import org.example.common.model.campaign.CampaignModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    @GetMapping("/create")
    public ResponseEntity<CampaignModel> createCampaign() {
        return new ResponseEntity<>((CampaignModel) ModelConverter.convert(campaignService.createCampaign(), CampaignModel.class), HttpStatus.OK);
    }
}
