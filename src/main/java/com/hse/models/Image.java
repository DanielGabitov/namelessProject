package com.hse.models;

public class Image {
    private int id;
    String eventName;
    private byte[] data;

    public Image(int id, String eventName, byte[] data) {
        this.id = id;
        this.eventName = eventName;
        this.data = data;
    }

    public Image(String eventName, byte[] data) {
        this.eventName = eventName;
        this.data = data;
    }

    public Image() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}