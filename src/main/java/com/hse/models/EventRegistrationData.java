package com.hse.models;

import java.util.List;
import java.util.Objects;

// {
//         "event" : {"name"            : "name",
//             "description"     : "description",
//             "images"          : [],
//             "organizerIDs"    : [],
//             "participantsIDs" : [],
//             "rating"          : 0.4,
//             "geoData"         : "geoData",
//             "specialization"  : "LITERATURE",
//             "date"            : "2020-04-04"
//         },
//         "images": ["data"]
// }

public class EventRegistrationData {
    private Event event;
    private List<String> images;

    public EventRegistrationData() {}

    public EventRegistrationData(Event event, List<String> images) {
        this.event = event;
        this.images = images;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
        return Objects.equals(event, that.event) && Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, images);
    }

    @Override
    public String toString() {
        return "EventRegistrationData{" +
                "event=" + event +
                ", images=" + images +
                '}';
    }
}
