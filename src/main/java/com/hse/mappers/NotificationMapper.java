package com.hse.mappers;

import com.hse.enums.NotificationType;
import com.hse.models.Notification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NotificationMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet resultSet, int i) throws SQLException {
        Notification notification = new Notification();
        notification.setNotificationId(resultSet.getLong("notificationId"));
        notification.setNotificationReceiverId(resultSet.getLong("notificationReceiverId"));
        notification.setNotificationProducerId(resultSet.getLong("notificationProducerId"));
        notification.setNotificationType(NotificationType.valueOf(resultSet.getString("notificationType")));
        return notification;
    }
}
