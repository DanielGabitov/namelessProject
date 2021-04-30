package com.hse.models;

import java.util.Objects;


public class User {
    private Long id;
    private String name;
    private String login;
    private String password;
    private double rating;

    public User() {
    }

    public User(String name, String login, String password, double rating) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.rating = rating;
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

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Double.compare(user.rating, rating) == 0 && Objects.equals(id, user.id) && Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, rating);
    }
}
