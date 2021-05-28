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

@Component
public class FeedService {

    private final EventDao eventDao;
    private final UserDao userDao;

    @Autowired
    public FeedService(EventDao eventDao, UserDao userDao) {
        this.eventDao = eventDao;
        this.userDao = userDao;
    }

    public List<Event> getEvents(int offset, int size) {
        return eventDao.getEvents(offset, size);
    }

    public List<User> getCreators(int offset, int size, EnumSet<Specialization> specializations) {
        if (specializations.isEmpty()) {
            return userDao.getCreators(offset, size);
        } else {
            return userDao.getCreators(offset, size, specializations);
        }
    }
}
