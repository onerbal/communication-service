package org.example.common.model.notification.email;

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
public class EmailNotificationModel extends NotificationModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private EmailNotificationTemplate template;

    private String subject;

    private String email;


}
