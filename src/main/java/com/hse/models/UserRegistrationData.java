package com.hse.models;

import java.util.List;
import java.util.Objects;

public class UserRegistrationData {
    private User user;
    private List<String> photos;

    public UserRegistrationData() {
    }

    public UserRegistrationData(User user, List<String> photos) {
        this.user = user;
        this.photos = photos;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRegistrationData that = (UserRegistrationData) o;
        return Objects.equals(user, that.user) && Objects.equals(photos, that.photos);
    }

    @Override
    public String toString() {
        return "UserRegistrationData{" +
                "user=" + user +
                ", photos=" + photos +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, photos);
    }
}
