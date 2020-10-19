package org.example.common.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.common.model.campaign.CampaignModel;
import org.example.common.model.user.UserModel;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCampaignNotificationEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    private UserModel user;

    private CampaignModel campaign;
}
