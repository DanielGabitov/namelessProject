package com.hse.models;

import com.hse.enums.Specialization;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;


public class Event {

    private Long id;
    private String name;
    private String description;
    private List<String> images;
    private List<Long> organizerIDs;
    private List<Long> participantsIDs;
    private double rating;
    private String geoData;
    private Specialization specialization;
    private Timestamp date;
    private List<Long> likes;

    public Event() {
    }

    public Event(Long id, String name, String description, List<String> images, List<Long> organizerIDs, List<Long> participantsIDs, double rating, String geoData, Specialization specialization, Timestamp date, List<Long> likes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.images = images;
        this.organizerIDs = organizerIDs;
        this.participantsIDs = participantsIDs;
        this.rating = rating;
        this.geoData = geoData;
        this.specialization = specialization;
        this.date = date;
        this.likes = likes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Long> getOrganizerIDs() {
        return organizerIDs;
    }

    public void setOrganizerIDs(List<Long> organizerIDs) {
        this.organizerIDs = organizerIDs;
    }

    public List<Long> getParticipantsIDs() {
        return participantsIDs;
    }

    public void setParticipantsIDs(List<Long> participantsIDs) {
        this.participantsIDs = participantsIDs;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGeoData() {
        return geoData;
    }

    public void setGeoData(String geoData) {
        this.geoData = geoData;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public List<Long> getLikes() {
        return likes;
    }

    public void setLikes(List<Long> likes) {
        this.likes = likes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.rating, rating) == 0 && Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(images, event.images) && Objects.equals(organizerIDs, event.organizerIDs) && Objects.equals(participantsIDs, event.participantsIDs) && Objects.equals(geoData, event.geoData) && specialization == event.specialization && Objects.equals(date, event.date) && Objects.equals(likes, event.likes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, images, organizerIDs, participantsIDs, rating, geoData, specialization, date, likes);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", images=" + images +
                ", organizerIDs=" + organizerIDs +
                ", participantsIDs=" + participantsIDs +
                ", rating=" + rating +
                ", geoData='" + geoData + '\'' +
                ", specialization=" + specialization +
                ", date=" + date +
                ", likes=" + likes +
                '}';
    }
}
