package com.hse.models;

import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationData {
    private UserRole userRole;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String username;
    private String password;
    private Specialization specialization;
    private float rating;
    private String description;
    private List<String> images;

//    json example
//    {
//        "userRole": "USER",
//            "firstName": "name",
//            "lastName": "name",
//            "patronymic": "name",
//            "username": "name1",
//            "password": "name",
//            "specialization": "ART",
//            "rating": 1,
//            "description": "description",
//            "images" : []
//    }
}
