package com.hse.services;

import com.hse.DAOs.EventDAO;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventService {
    private final EventDAO eventDAO;

    @Autowired
    public EventService(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public void saveEvent(Event event) throws ServiceException {
        try {
            eventDAO.saveEvent(event);
        } catch (DataAccessException e) {
            throw new ServiceException("Failed to save event to a DataBase", e);
        }
    }

    public Event getEvent(long id) throws ServiceException {
        try {
            List<Event> eventList = eventDAO.getEvent(id);
            if (eventList.isEmpty()) {
                throw new ServiceException("Failed to find event with given ID.");
            }
            return eventList.get(0);
        } catch (DataAccessException exception) {
            throw new ServiceException("Failed to access a database.", exception);
        }
    }

    public void addImages(long id, List<String> imageHashes) throws ServiceException {
        Event event = getEvent(id);
        event.getImageHashes().addAll(imageHashes);
        try {
            eventDAO.updateImageHashes(id, event.getImageHashes());
        } catch (DataAccessException exception){
            throw new ServiceException("Failed to update event in a database.", exception);
        }
    }
}
