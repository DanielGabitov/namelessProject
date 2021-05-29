package com.hse.models;

import com.hse.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private Long notificationId;
    private Long notificationReceiverId;
    private Long notificationProducerId;
    private NotificationType notificationType;
}
