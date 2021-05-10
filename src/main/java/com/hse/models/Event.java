package com.hse.models;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import com.hse.enums.Specialization;


public class Event {

    private Long id;
    private String name;
    private String description;
    private List<String> imageHashes;
    private List<Long> organizerIDs;
    private List<Long> participantsIDs;
    private double rating;
    private String geoData;
    private Specialization specialization;
    private Timestamp date;

    public Event() {
    }

    //*
//     {
//     "name"            : "name",
//     "description"     : "description",
//     "imageHashes"     : ["hash#1, hash#2"],
//     "organizerIDs"    : [1, 2],
//     "participantsIDs" : [1, 2],
//     "rating"          : 0.4,
//     "geoData"         : "geoData",
//     "specialization"  : "specialization",
//     "date"            : "04-23-17 04:34:22"
//     }
    //
    //
    //
    // *//

    public Event(Long id, String name, String description,
                 List<String> imageHashes, List<Long> organizerIDs, List<Long> participantsIDs, double rating,
                 String geoData, Specialization specialization, Timestamp date) {

        this.id = id;
        this.name = name;
        this.description = description;
        this.imageHashes = imageHashes;
        this.organizerIDs = organizerIDs;
        this.participantsIDs = participantsIDs;
        this.rating = rating;
        this.geoData = geoData;
        this.specialization = specialization;
        this.date = date;
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

    public List<String> getImageHashes() {
        return imageHashes;
    }

    public void setImageHashes(List<String> imageHashes) {
        this.imageHashes = imageHashes;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Double.compare(event.rating, rating) == 0 && Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(imageHashes, event.imageHashes) && Objects.equals(organizerIDs, event.organizerIDs) && Objects.equals(participantsIDs, event.participantsIDs) && Objects.equals(geoData, event.geoData) && specialization == event.specialization && Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, imageHashes, organizerIDs, participantsIDs, rating, geoData, specialization, date);
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageHashes=" + imageHashes +
                ", organizerIDs=" + organizerIDs +
                ", participantsIDs=" + participantsIDs +
                ", rating=" + rating +
                ", geoData='" + geoData + '\'' +
                ", specialization=" + specialization +
                ", date=" + date +
                '}';
    }
}
