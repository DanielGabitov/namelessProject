package com.hse.services;

import com.hse.DAOs.SubscriptionDao;
import com.hse.enums.UserRole;
import com.hse.exceptions.ServiceException;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SubscriptionService {
    private final UserService userService;
    private final SubscriptionDao subscriptionDao;

    @Autowired
    public SubscriptionService(UserService userService, SubscriptionDao subscriptionDao) {
        this.userService = userService;
        this.subscriptionDao = subscriptionDao;
    }

    public void addSubscription(Long userId, Long subscriptionId) {
        if (userId.equals(subscriptionId)) {
            throw new ServiceException("User cannot subscribe to himself.");
        } else if (checkSubscription(userId, subscriptionId)) {
            throw new ServiceException("Subscription already exist.");
        }
        User subscription = userService.loadUserById(subscriptionId);
        if (subscription.getUserRole().equals(UserRole.USER)) {
            throw new SecurityException("There is no way to subscribe to the user.");
        }
        subscriptionDao.addSubscription(userId, subscriptionId);
    }

    public boolean checkSubscription(Long userId, Long subscriptionId) {
        return subscriptionDao.checkSubscription(userId, subscriptionId);
    }

    public void deleteSubscription(Long userId, Long subscriptionId) {
        if (!checkSubscription(userId, subscriptionId)) {
            throw new ServiceException("Subscription dost not exist.");
        }
        subscriptionDao.deleteSubscription(userId, subscriptionId);
    }

    public List<User> getAllSubscriptions(Long userId) {
        return subscriptionDao.getAllSubscriptionIds(userId).stream()
                .map(userService::loadUserById)
                .collect(Collectors.toList());
    }
}
