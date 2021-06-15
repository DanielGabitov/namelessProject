package com.hse.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreativeAssociationRegistrationData {
    private String name;
    private String description;
    private Long bossCreator;
    private List<String> images;
}

//    json example
//    {
//            "name"            : "name",
//            "description"     : "description",
//            "bossCreator"     : 1,
//            "images"          : ["some data #1", "some data #2"]
//    }
