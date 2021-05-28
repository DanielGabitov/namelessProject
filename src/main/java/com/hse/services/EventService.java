package com.hse.services;

import com.hse.DAOs.*;
import com.hse.exceptions.ServiceException;
import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class EventService {
    private final EventDao eventDAO;
    private final EventToOrganizerDao eventToOrganizerDAO;
    private final EventToParticipantDao eventToParticipantDAO;
    private final EventToImagesDao eventToImagesDAO;
    private final LikesDao likesDAO;

    @Autowired
    public EventService(EventDao eventDAO, EventToOrganizerDao eventToOrganizerDAO,
                        EventToParticipantDao eventToParticipantDAO, EventToImagesDao eventToImagesDAO,
                        LikesDao likesDao) {

        this.eventDAO = eventDAO;
        this.eventToOrganizerDAO = eventToOrganizerDAO;
        this.eventToParticipantDAO = eventToParticipantDAO;
        this.eventToImagesDAO = eventToImagesDAO;
        this.likesDAO = likesDao;
    }

    @Transactional
    public void createEvent(EventRegistrationData eventRegistrationData) {
        Event event = readRegistrationData(eventRegistrationData);
        long eventId = eventDAO.createEvent(event);
        addParticipants(eventId, event.getParticipantsIDs());
        addOrganizers(eventId, event.getOrganizerIDs());
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(eventRegistrationData.getImages());
        addImages(eventId, imageUUIDs);
    }

    @Transactional
    public Event getEvent(long id) {
        Optional<Event> optionalEvent = eventDAO.getEvent(id);
        if (optionalEvent.isEmpty()) {
            throw new ServiceException("Failed to find event with given ID.");
        }
        Event event = optionalEvent.get();
        setEventDataFromOtherTables(event);
        return event;
    }

    public void setEventDataFromOtherTables(Event event) {
        Long id = event.getId();
        event.setParticipantsIDs(getParticipants(id));
        event.setOrganizerIDs(getOrganizers(id));
        event.setImages(getImages(id));
        event.setLikes(getLikes(id));
    }

    public void addParticipants(long eventId, List<Long> participants) {
        participants.forEach(participant -> eventToParticipantDAO.addParticipant(eventId, participant));
    }

    public List<Long> getParticipants(long eventId) {
        return eventToParticipantDAO.getParticipants(eventId);
    }

    public void addOrganizers(long eventId, List<Long> organizers) {
        organizers.forEach(organizer -> eventToOrganizerDAO.addOrganizer(eventId, organizer));
    }

    public List<Long> getOrganizers(long eventId) {
        return eventToOrganizerDAO.getOrganizers(eventId);
    }

    public void addImages(long eventId, List<String> imageUUIDs) {
        imageUUIDs.forEach(image -> eventToImagesDAO.addImage(eventId, image));
    }

    public List<String> getImages(long eventId) {
        return eventToImagesDAO.getImages(eventId).stream()
                .map(FileSystemInteractor::getImage)
                .map(Coder::encode)
                .collect(Collectors.toList());
    }

    public List<Long> getLikes(long eventId) {
        return likesDAO.getEventLikes(eventId);
    }

    private Event readRegistrationData(EventRegistrationData data) {
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
