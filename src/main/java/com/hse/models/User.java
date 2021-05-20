package com.hse.models;

import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;


public class User implements UserDetails {

    private long id;
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String username;
    private String password;
    private Specialization specialization;
    private double rating;
    private String description;
    private List<String> images;

    public User() {
    }

    public User(long id, UserRole userRole, String firstName, String lastName, String patronymic, String username,
                String password, Specialization specialization, double rating, String description, List<String> images,
                List<Long> eventsId) {
        this.id = id;
        this.userRole = userRole;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.username = username;
        this.password = password;
        this.specialization = specialization;
        this.rating = rating;
        this.description = description;
        this.images = images;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Specialization getSpecialization() {
        return specialization;
    }

    public void setSpecialization(Specialization specialization) {
        this.specialization = specialization;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Double.compare(user.rating, rating) == 0 && userRole == user.userRole && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(patronymic, user.patronymic) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(specialization, user.specialization) && Objects.equals(description, user.description) && Objects.equals(images, user.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userRole, firstName, lastName, patronymic, username, password, specialization, rating, description, images);
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", userRole=" + userRole +
                ", name='" + firstName + '\'' +
                ", secondName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", specialization=" + specialization +
                ", rating=" + rating +
                ", description='" + description + '\'' +
                ", images=" + images +
                '}';
    }
}