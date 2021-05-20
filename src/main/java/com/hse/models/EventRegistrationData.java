package com.hse.models;

import com.hse.enums.Specialization;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class EventRegistrationData {

    private String name;
    private String description;
    private Double rating;
    private String geoData;
    private Specialization specialization;
    private Timestamp date;
    private List<String> images;

//    json example
//    {
//            "name"            : "name",
//            "description"     : "description",
//            "rating"          : 0.4,
//            "geoData"         : "geoData",
//            "specialization"  : "LITERATURE",
//            "date"            : "2020-04-04",
//            "images"          : ["some data #1", "some data #2"]
//    }

    public EventRegistrationData() {}

    public EventRegistrationData(String name, String description, Double rating, String geoData, Specialization specialization, Timestamp date, List<String> images) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.geoData = geoData;
        this.specialization = specialization;
        this.date = date;
        this.images = images;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventRegistrationData that = (EventRegistrationData) o;
        return Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(rating, that.rating) && Objects.equals(geoData, that.geoData) && specialization == that.specialization && Objects.equals(date, that.date) && Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, rating, geoData, specialization, date, images);
    }

    @Override
    public String toString() {
        return "EventRegistrationData{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rating=" + rating +
                ", geoData='" + geoData + '\'' +
                ", specialization=" + specialization +
                ", Timestamp='" + date + '\'' +
                ", images=" + images +
                '}';
    }
}
