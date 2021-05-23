package com.hse.controllers;

import com.hse.models.Event;
import com.hse.services.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    private FeedController(FeedService feedService){
        this.feedService = feedService;
    }

    @GetMapping(consumes = {"application/json"})
    public List<Event> getEvents(@RequestParam("offset") int offset, @RequestParam("size") int size){
        return feedService.getEvents(offset, size);
    }
}
