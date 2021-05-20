package com.hse.models;

import com.hse.enums.Entity;

import java.util.List;
import java.util.Objects;

public class ImageRegistrationData {

    private Entity destination;
    private List<String> images;
    private long destinationId;

    public ImageRegistrationData() {
    }

    public ImageRegistrationData(Entity destination, List<String> images, long destinationId) {
        this.destination = destination;
        this.images = images;
        this.destinationId = destinationId;
    }

    public Entity getDestination() {
        return destination;
    }

    public void setDestination(Entity destination) {
        this.destination = destination;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public long getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(long destinationId) {
        this.destinationId = destinationId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageRegistrationData that = (ImageRegistrationData) o;
        return destinationId == that.destinationId && destination == that.destination && Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destination, images, destinationId);
    }

    @Override
    public String toString() {
        return "ImageRegistrationData{" +
                "destination=" + destination +
                ", images=" + images +
                ", destinationId=" + destinationId +
                '}';
    }
}
