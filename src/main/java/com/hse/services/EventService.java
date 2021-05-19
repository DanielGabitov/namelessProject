package com.hse.services;

import com.hse.DAOs.EventDAO;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import com.hse.utils.UUIDGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventService {
    private final EventDAO eventDAO;

    @Autowired
    public EventService(EventDAO eventDAO) {
        this.eventDAO = eventDAO;
    }

    public void saveEvent(EventRegistrationData eventRegistrationData) {
        Event event = eventRegistrationData.getEvent();
        long eventId = eventDAO.saveEvent(event);

        // todo these lines appear in saveUser as well but Im not sure whether its a good idea to create 1 method for them
        List<String> encodedImages = eventRegistrationData.getImages();
        List<byte[]> images = ImageService.decodeImages(encodedImages);
        List<String> imageUUIDs = UUIDGenerator.generateList(images.size());
        ImageService.saveImagesToFileSystem(images, imageUUIDs);

        addImages(eventId, imageUUIDs);
    }

    public Event getEvent(long id) {
        List<Event> eventList = eventDAO.getEvent(id);
        if (eventList.isEmpty()) {
            throw new ServiceException("Failed to find event with given ID.");
        }
        return eventList.get(0);
    }

    //todo deleteImages
    public void addImages(long id, List<String> imageUUIDs) {
        eventDAO.updateImageHashes(id, imageUUIDs);
    }
}
