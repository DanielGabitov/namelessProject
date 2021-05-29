package com.hse.services;

import com.hse.DAOs.SubscriptionDao;
import com.hse.enums.UserRole;
import com.hse.exceptions.ServiceException;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    public void addSubscription(long userId, long subscriptionId) {
        if (userId == subscriptionId) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "User cannot subscribe to himself.");
        } else if (checkSubscription(userId, subscriptionId)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Subscription already exist.");
        }
        User userToSubscribe = userService.loadUserById(subscriptionId);
        if (userToSubscribe.getUserRole().equals(UserRole.USER)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "There is no way to subscribe to the user.");
        }
        subscriptionDao.addSubscription(userId, subscriptionId);
    }

    public boolean checkSubscription(long userId, long subscriptionId) {
        return subscriptionDao.checkSubscription(userId, subscriptionId);
    }

    public void deleteSubscription(long userId, long subscriptionId) {
        if (!checkSubscription(userId, subscriptionId)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "Subscription dost not exist.");
        }
        subscriptionDao.deleteSubscription(userId, subscriptionId);
    }

    public List<User> getAllSubscriptions(long userId) {
        return subscriptionDao.getAllSubscriptionIds(userId).stream()
                .map(userService::loadUserById)
                .collect(Collectors.toList());
    }
}
