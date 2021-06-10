package com.hse.services;

import com.hse.DAOs.EventDao;
import com.hse.DAOs.RecommendationDao;
import com.hse.DAOs.UserDao;
import com.hse.enums.Specialization;
import com.hse.models.Event;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FeedService {

    private final EventDao eventDao;
    private final UserDao userDao;
    private final RecommendationDao recommendationDao;
    private final UserService userService;
    private final EventService eventService;

    @Autowired
    public FeedService(EventDao eventDao, UserDao userDao, UserService userService,
                       EventService eventService, RecommendationDao recommendationDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
        this.recommendationDao = recommendationDao;
        this.userService = userService;
        this.eventService = eventService;
    }

    public List<Event> getEvents(int offset, int size, EnumSet<Specialization> specializations) {
        List<Event> events;
        if (specializations.isEmpty()) {
            events = eventDao.getEvents(offset, size);
        } else {
            events = eventDao.getEvents(offset, size, specializations);
        }
        return events.stream()
                .peek(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<Event> getEventRecommendations(long userId, int offset,
                                               int size, EnumSet<Specialization> specializations) {
        if (!recommendationDao.checkIfUserHasRecommendations(userId)){
            return getEvents(offset, size, specializations);
        }
        List<Event> events;
        if (specializations.isEmpty()) {
            events = eventDao.getRecommendedEvents(offset, size, userId);
        } else {
            events = eventDao.getRecommendedEvents(offset, size, userId, specializations);
        }
        return events.stream()
                .peek(eventService::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<User> getCreators(int offset, int size, EnumSet<Specialization> specializations) {
        List<User> creators;
        if (specializations.isEmpty()) {
            creators = userDao.getCreators(offset, size);
        } else {
            creators = userDao.getCreators(offset, size, specializations);
        }
        return creators.stream()
                .peek(user -> user.setImages(userService.getImages(user.getId())))
                .collect(Collectors.toList());
    }
}
