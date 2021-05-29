package com.hse.DAOs;

import com.hse.enums.NotificationType;
import com.hse.mappers.NotificationMapper;
import com.hse.models.Notification;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationDao {
    private final NamedParameterJdbcTemplate namedJdbcTemplate;
    private final NotificationMapper notificationMapper;

    public NotificationDao(NamedParameterJdbcTemplate namedJdbcTemplate, NotificationMapper notificationMapper){
        this.namedJdbcTemplate = namedJdbcTemplate;
        this.notificationMapper = notificationMapper;
    }

    public void addNotification(long notificationReceiverId, long notificationProducerId,
                                NotificationType notificationType){

        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("notificationReceiverId", notificationReceiverId);
        map.addValue("notificationProducerId", notificationProducerId);
        map.addValue("notificationType", notificationType.name());

        namedJdbcTemplate.update(
                "INSERT INTO notifications (notificationReceiverId, notificationProducerId, notificationType)" +
                    " VALUES (:notificationReceiverId, :notificationProducerId, :notificationType)", map);
    }

    public List<Notification> getUserNotifications(long userId){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("notificationReceiverId", userId);
        return namedJdbcTemplate.query(
                "SELECT * FROM notifications WHERE notificationReceiverId = :notificationReceiverId",
                    map, notificationMapper);
    }

    public void deleteNotifications(List<Long> notificationIds){
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("notificationIds", notificationIds);
        namedJdbcTemplate.update("DELETE FROM notifications WHERE notificationid IN (:notificationIds)", map);
    }
}
