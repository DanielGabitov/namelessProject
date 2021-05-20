package com.hse.services;

import com.hse.DAOs.*;
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
    private final EventToOrganizerDAO eventToOrganizerDAO;
    private final EventToParticipantDAO eventToParticipantDAO;
    private final EventToImagesDAO eventToImagesDAO;
    private final LikesDAO likesDAO;

    @Autowired
    public EventService(EventDAO eventDAO, EventToOrganizerDAO eventToOrganizerDAO,
                        EventToParticipantDAO eventToParticipantDAO, EventToImagesDAO eventToImagesDAO,
                        LikesDAO likesDao) {

        this.eventDAO = eventDAO;
        this.eventToOrganizerDAO = eventToOrganizerDAO;
        this.eventToParticipantDAO = eventToParticipantDAO;
        this.eventToImagesDAO = eventToImagesDAO;
        this.likesDAO = likesDao;
    }

    public void saveEvent(EventRegistrationData eventRegistrationData) {
        Event event  = readRegistrationData(eventRegistrationData);
        long eventId = eventDAO.saveEvent(event);
        addParticipants(eventId, event.getParticipantsIDs());
        addOrganizers(eventId, event.getOrganizerIDs());

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
        Event event = eventList.get(0);
        event.setParticipantsIDs(getParticipants(id));
        event.setOrganizerIDs(getOrganizers(id));
        event.setImages(getImages(id));
        event.setLikes(getLikes(id));
        return event;
    }

    public void addParticipants(long eventId, List<Long> participants) {
        for (Long participant : participants) {
            eventToParticipantDAO.addParticipant(eventId, participant);
        }
    }

    public List<Long> getParticipants(long eventId) {
        return eventToParticipantDAO.getParticipants(eventId);
    }

    public void addOrganizers(long eventId, List<Long> organizers) {
        for (Long organizer : organizers) {
            eventToOrganizerDAO.addOrganizer(eventId, organizer);
        }
    }

    public List<Long> getOrganizers(long eventId) {
        return eventToOrganizerDAO.getOrganizers(eventId);
    }

    public void addImages(long eventId, List<String> imageUUIDs) {
        for (String image : imageUUIDs) {
            eventToImagesDAO.addImage(eventId, image);
        }
    }

    public List<String> getImages(long eventId) {
        return eventToImagesDAO.getImages(eventId);
    }

    public List<Long> getLikes(long eventId) {
        return likesDAO.getEventLikes(eventId);
    }

    private Event readRegistrationData(EventRegistrationData data){
        Event event = new Event();
        event.setName(data.getName());
        event.setDescription(data.getDescription());
        event.setRating(data.getRating());
        event.setGeoData(data.getGeoData());
        event.setSpecialization(data.getSpecialization());
        event.setDate(data.getDate());

        event.setLikes(List.of());
        event.setImages(List.of());
        event.setOrganizerIDs(List.of());
        event.setParticipantsIDs(List.of());

        return event;
    }
}
