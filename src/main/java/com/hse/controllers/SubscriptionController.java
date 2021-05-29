package com.hse.controllers;

import com.hse.models.User;
import com.hse.services.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @Autowired
    public SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @PostMapping
    public ResponseEntity<String> addSubscription(@RequestParam("userId") Long userId,
                                                  @RequestParam("subscriptionId") Long subscriptionId) {
        subscriptionService.addSubscription(userId, subscriptionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteSubscription(@RequestParam("userId") Long userId,
                                                     @RequestParam("subscriptionId") Long subscriptionId) {
        subscriptionService.deleteSubscription(userId, subscriptionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllSubscriptions(@RequestParam("userId") Long userId) {
        List<User> subscriptionIds = subscriptionService.getAllSubscriptions(userId);
        return new ResponseEntity<>(subscriptionIds, HttpStatus.OK);
    }
}
