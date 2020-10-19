package org.example.common.model.notification.push;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.common.model.notification.NotificationModel;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PushNotificationModel extends NotificationModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private DeviceType deviceType;

    private String deviceId;
}

