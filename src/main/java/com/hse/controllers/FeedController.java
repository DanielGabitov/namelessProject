package com.hse.controllers;

import com.hse.enums.Specialization;
import com.hse.models.Event;
import com.hse.models.User;
import com.hse.services.FeedService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.EnumSet;
import java.util.List;

@RestController
@RequestMapping("/api/feed")
public class FeedController {

    private final FeedService feedService;

    @Autowired
    private FeedController(FeedService feedService) {
        this.feedService = feedService;
    }

    @GetMapping(value = "/events")
    @ApiOperation(value = "/events", nickname = "Get events for feed", tags = {"Feed"})
    public List<Event> getEvents(@RequestParam("offset") int offset, @RequestParam("size") int size,
                                 @RequestParam("specializations") EnumSet<Specialization> specializations) {
        return feedService.getEvents(offset, size, specializations);
    }

    @GetMapping(value = "/{userId}/events/recommendations")
    @ApiOperation(value = "/events", nickname = "Get events for feed", tags = {"Feed"})
    public List<Event> getEventRecommendations(@RequestParam("offset") int offset,  @RequestParam("size") int size,
                                               @PathVariable("userId") long userId,
                                               @RequestParam("specializations") EnumSet<Specialization> specializations) {
        return feedService.getEventRecommendations(userId, offset, size, specializations);
    }

    @GetMapping(value = "/creators")
    @ApiOperation(value = "/creators", nickname = "Get creators for feed", tags = {"Feed"})
    public List<User> getCreators(@RequestParam("offset") int offset, @RequestParam("size") int size,
                                  @RequestParam("specializations") EnumSet<Specialization> specializations) {
        return feedService.getCreators(offset, size, specializations);
    }
}
