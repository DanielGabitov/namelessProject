package com.hse.controllers;

import com.hse.enums.Specialization;
import com.hse.models.CreativeAssociation;
import com.hse.models.Event;
import com.hse.models.User;
import com.hse.services.FeedService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Event>> getEvents(@RequestParam("offset") int offset, @RequestParam("size") int size,
                                                 @RequestParam("specializations") EnumSet<Specialization> specializations) {
        return new ResponseEntity<>(feedService.getEvents(offset, size, specializations), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}/events/recommendations")
    @ApiOperation(value = "/events", nickname = "Get events for feed", tags = {"Feed"})
    public ResponseEntity<List<Event>> getEventRecommendations(@RequestParam("offset") int offset, @RequestParam("size") int size,
                                                               @PathVariable("userId") long userId,
                                                               @RequestParam("specializations") EnumSet<Specialization> specializations) {
        return new ResponseEntity<>(feedService.getEventRecommendations(userId, offset, size, specializations), HttpStatus.OK);
    }

    @GetMapping(value = "/creators")
    @ApiOperation(value = "/creators", nickname = "Get creators for feed", tags = {"Feed"})
    public ResponseEntity<List<User>> getCreators(@RequestParam("offset") int offset, @RequestParam("size") int size,
                                                  @RequestParam("specializations") EnumSet<Specialization> specializations) {
        return new ResponseEntity<>(feedService.getCreators(offset, size, specializations), HttpStatus.OK);
    }

    @GetMapping(value = "/creativeAssociations")
    @ApiOperation(value = "/creators", nickname = "Get creators for feed", tags = {"Feed"})
    public ResponseEntity<List<CreativeAssociation>> getCreativeAssociations(@RequestParam("offset") int offset, @RequestParam("size") int size) {
        return new ResponseEntity<>(feedService.getCreativeAssociations(offset, size), HttpStatus.OK);
    }
}
