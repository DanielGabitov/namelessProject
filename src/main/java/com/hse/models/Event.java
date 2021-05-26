package com.hse.models;

import com.hse.enums.Specialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Event {

    private Long id;
    private String name;
    private String description;
    private List<String> images;
    private List<Long> organizerIDs;
    private List<Long> participantsIDs;
    private float rating;
    private String geoData;
    private Specialization specialization;
    private Timestamp date;
    private List<Long> likes;
}
