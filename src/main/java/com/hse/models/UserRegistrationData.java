package com.hse.models;

import java.util.List;
import java.util.Objects;

public class UserRegistrationData {
    private User user;
    private List<String> images;

    public UserRegistrationData() {
    }

    public UserRegistrationData(User user, List<String> images) {
        this.user = user;
        this.images = images;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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
        UserRegistrationData that = (UserRegistrationData) o;
        return Objects.equals(user, that.user) && Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, images);
    }

    @Override
    public String toString() {
        return "UserRegistrationData{" +
                "user=" + user +
                ", images=" + images +
                '}';
    }
}
