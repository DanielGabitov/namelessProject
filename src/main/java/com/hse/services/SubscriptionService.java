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
        }
        if (!checkIfSubscriptionExists(userId, subscriptionId)) {
            User userToSubscribe = userService.loadUserById(subscriptionId);
            if (userToSubscribe.getUserRole().equals(UserRole.USER)) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "There is no way to subscribe to the user.");
            }
            if (!userService.checkIfUserExists(subscriptionId)) {
                throw new ServiceException(HttpStatus.BAD_REQUEST, "There is no user with such id.");
            }
            subscriptionDao.addSubscription(userId, subscriptionId);
        }
    }

    public boolean checkIfSubscriptionExists(long userId, long subscriptionId) {
        Integer numberOfUserSubscriptionsById = subscriptionDao.getNumberOfUserSubscriptionsWithSuchId(userId, subscriptionId);
        return numberOfUserSubscriptionsById != null && numberOfUserSubscriptionsById != 0;
    }

    public void deleteSubscription(long userId, long subscriptionId) {
        if (checkIfSubscriptionExists(userId, subscriptionId)) {
            subscriptionDao.deleteSubscription(userId, subscriptionId);
        }
    }

    public List<User> getAllSubscriptions(long userId) {
        return subscriptionDao.getAllSubscriptionIds(userId).stream()
                .map(userService::loadUserById)
                .collect(Collectors.toList());
    }
}
