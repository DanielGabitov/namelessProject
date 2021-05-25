package com.hse.services;

import com.hse.DAOs.EventDao;
import com.hse.models.Event;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedService {

    private final EventDao eventDao;
    private final EventService eventService;

    public FeedService(EventDao eventDao, EventService eventService) {
        this.eventDao = eventDao;
        this.eventService = eventService;
    }

    public List<Event> getEvents(int offset, int size) {
        List<Long> eventIds = eventDao.getEvents(offset, size);
        return eventIds.stream().map(eventService::getEvent).collect(Collectors.toList());
    }
}
