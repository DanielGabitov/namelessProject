package com.hse.models;

import java.sql.Timestamp;
import java.util.Objects;


public class Event {
    private int id;
    private String name;
    private String description;
    private Timestamp date;
    private int organizerId;

    public Event() {}

    public Event(String name, String description, Timestamp date, int organizerId) {
        this.name = name;
        this.description = description;
        this.date = date;
        this.organizerId = organizerId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public Timestamp getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(int organizerId) {
        this.organizerId = organizerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(date, event.date) && Objects.equals(organizerId, event.organizerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, date, organizerId);
    }
}
