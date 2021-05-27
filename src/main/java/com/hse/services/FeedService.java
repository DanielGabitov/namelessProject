package com.hse.services;

import com.hse.DAOs.EventDao;
import com.hse.DAOs.UserDao;
import com.hse.enums.Specialization;
import com.hse.models.Event;
import com.hse.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FeedService {

    private final EventDao eventDao;
    private final EventService eventService;

    private final UserDao userDao;
    private final UserService userService;

    @Autowired
    public FeedService(EventDao eventDao, EventService eventService, UserDao userDao, UserService userService) {
        this.eventDao = eventDao;
        this.eventService = eventService;
        this.userDao = userDao;
        this.userService = userService;
    }

    public List<Event> getEvents(int offset, int size) {
        List<Long> eventIds = eventDao.getEvents(offset, size);
        return eventIds.stream().map(eventService::getEvent).collect(Collectors.toList());
    }

    public List<User> getCreators(int offset, int size, EnumSet<Specialization> specializations){
        List<Long> creatorsIds;
        if (specializations.isEmpty()){
            creatorsIds = userDao.getCreators(offset, size);
        } else {
            creatorsIds = userDao.getCreators(offset, size, specializations);
        }
        return creatorsIds.stream().map(userService::loadUserById).collect(Collectors.toList());
    }
}
