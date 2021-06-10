package com.hse.services;

import com.hse.DAOs.EventDao;
import com.hse.DAOs.EventToImagesDao;
import com.hse.DAOs.EventToParticipantDao;
import com.hse.DAOs.LikesDao;
import com.hse.exceptions.ServiceException;
import com.hse.models.Application;
import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import com.hse.models.User;
import com.hse.systems.FileSystemInteractor;
import com.hse.utils.Coder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventService {
    private final EventDao eventDao;
    private final EventToParticipantDao eventToParticipantDao;
    private final EventToImagesDao eventToImagesDao;
    private final LikesDao likesDao;

    @Autowired
    public EventService(EventDao eventDao, EventToParticipantDao eventToParticipantDao,
                        EventToImagesDao eventToImagesDao, LikesDao likesDao) {

        this.eventDao = eventDao;
        this.eventToParticipantDao = eventToParticipantDao;
        this.eventToImagesDao = eventToImagesDao;
        this.likesDao = likesDao;
    }

    @Transactional
    public void createEvent(EventRegistrationData eventRegistrationData) {
        Event event = readRegistrationData(eventRegistrationData);
        long eventId = eventDao.createEvent(event);
        addParticipants(eventId, event.getParticipantsIDs());
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(eventRegistrationData.getImages());
        addImages(eventId, imageUUIDs);
    }

    @Transactional
    public void updateEvent(long eventId, Event event) {
        if (!eventDao.checkEvent(eventId)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, "There is no event with such id.");
        }
        eventDao.updateEvent(eventId, event);
        eventToParticipantDao.deleteParticipants(eventId);
        addParticipants(eventId, event.getParticipantsIDs());
        List<String> eventImages = eventToImagesDao.getImages(eventId);
        ImageService.deleteImages(eventImages);
        eventToImagesDao.deleteEventImages(eventId);
        List<String> imageUUIDs = ImageService.saveImagesToFileSystem(event.getImages());
        addImages(eventId, imageUUIDs);
    }

    @Transactional
    public Event getEvent(long eventId) {
        Event event = getEventFromEventTable(eventId);
        setEventDataFromOtherTables(event);
        return event;
    }

    private Event getEventFromEventTable(long eventId){
        return eventDao.getEvent(eventId).orElseThrow(
                () -> new ServiceException(HttpStatus.BAD_REQUEST, "Failed to find event with given ID.")
        );
    }

    public Long getEventOrganizer(long eventId){
        return getEventFromEventTable(eventId).getOrganizerId();
    }

    public void setEventDataFromOtherTables(Event event) {
        Long id = event.getId();
        event.setParticipantsIDs(getParticipantsIds(id));
        event.setImages(getImages(id));
        event.setLikes(getLikes(id));
    }

    public void addParticipants(long eventId, List<Long> participants) {
        participants.forEach(participant -> eventToParticipantDao.addParticipant(eventId, participant));
    }

    public List<Long> getParticipantsIds(long eventId) {
        return eventToParticipantDao.getParticipants(eventId);
    }

    public void addImages(long eventId, List<String> imageUUIDs) {
        imageUUIDs.forEach(image -> eventToImagesDao.addImage(eventId, image));
    }

    public List<String> getImages(long eventId) {
        return eventToImagesDao.getImages(eventId).stream()
                .map(FileSystemInteractor::getImage)
                .map(Coder::encode)
                .collect(Collectors.toList());
    }

    public List<Long> getLikes(long eventId) {
        return likesDao.getEventLikes(eventId);
    }

    public List<Application> getEventApplications(long eventId){
        return eventDao.getEventApplications(eventId);
    }

    public List<Event> getFutureEvents(List<Long> eventIds){
        return eventDao.getAllFutureEvents(eventIds).stream()
                .peek(this::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    public List<Event> getPassedEvents(List<Long> eventIds){
        return eventDao.getAllPassedEvents(eventIds).stream()
                .peek(this::setEventDataFromOtherTables)
                .collect(Collectors.toList());
    }

    private Event readRegistrationData(EventRegistrationData data) {
        Event event = new Event();
        event.setName(data.getName());
        event.setDescription(data.getDescription());
        event.setRating(data.getRating());
        event.setGeoData(data.getGeoData());
        event.setSpecialization(data.getSpecialization());
        event.setDate(data.getDate());
        event.setOrganizerId(data.getOrganizerId());
        event.setLikes(List.of());
        event.setImages(List.of());
        event.setParticipantsIDs(List.of());

        return event;
    }
}
