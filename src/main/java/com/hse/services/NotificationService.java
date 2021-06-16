package com.hse.services;

import com.hse.DAOs.NotificationDao;
import com.hse.enums.NotificationType;
import com.hse.models.Notification;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationService {

    private final NotificationDao notificationDao;

    public NotificationService(NotificationDao notificationDao) {
        this.notificationDao = notificationDao;
    }

    public void sendNewInvitationNotification(long creatorId, long organizerId) {
        notificationDao.addNotification(creatorId, organizerId, NotificationType.NEW_INVITATION);
    }

    public void sendInvitationAnswerNotification(long creatorId, long organizerId, boolean accepted) {
        NotificationType notificationType;
        if (accepted) {
            notificationType = NotificationType.INVITATION_ANSWER_ACCEPTED;
        } else {
            notificationType = NotificationType.INVITATION_ANSWER_REJECTED;
        }
        notificationDao.addNotification(organizerId, creatorId, notificationType);
    }

    public void sendNewApplicationNotification(long creatorId, long organizerId) {
        notificationDao.addNotification(organizerId, creatorId, NotificationType.NEW_APPLICATION);
    }

    public void sendApplicationAnswerNotification(long creatorId, long eventId, boolean accepted) {
        NotificationType notificationType;
        if (accepted) {
            notificationType = NotificationType.APPLICATION_ANSWER_ACCEPTED;
        } else {
            notificationType = NotificationType.APPLICATION_ANSWER_REJECTED;
        }
        notificationDao.addNotification(creatorId, eventId, notificationType);
    }

    public void sendCreativeAssociationInvitationNotification(long associationBossCreatorId, long creatorId){
        notificationDao.addNotification(creatorId, associationBossCreatorId, NotificationType.NEW_CREATIVE_ASSOCIATION_INVITATION);
    }

    public void sendCreativeAssociationInvitationAnswerNotification(long associationBossCreatorId,
                                                                    long creatorId,
                                                                    boolean accepted){
        NotificationType notificationType;
        if (accepted) {
            notificationType = NotificationType.CREATIVE_ASSOCIATION_INVITATION_ANSWER_ACCEPTED;
        } else {
            notificationType = NotificationType.CREATIVE_ASSOCIATION_INVITATION_ANSWER_REJECTED;
        }
        notificationDao.addNotification(associationBossCreatorId, creatorId, notificationType);
    }

    public void sendNotificationToRatePassedEvent(long passedEventId, long userId){
        notificationDao.addNotification(userId, passedEventId, NotificationType.RATE_PASSED_EVENT);
    }

    public List<Notification> getUserNotifications(long userId) {
        return notificationDao.getUserNotifications(userId);
    }

    public void deleteNotifications(List<Long> notificationsId) {
        notificationDao.deleteNotifications(notificationsId);
    }
}
