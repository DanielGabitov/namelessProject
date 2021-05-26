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
public class EventRegistrationData {

    private String name;
    private String description;
    private float rating;
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
}
