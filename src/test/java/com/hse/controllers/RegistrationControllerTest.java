package com.hse.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import com.hse.models.UserRegistrationData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class RegistrationControllerTest {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @Autowired
    public RegistrationControllerTest(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

//    @Test
//    @Sql(value = {"/scripts/before-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    public void testRegistrationUser() throws Exception {
//        UserRegistrationData userRegistrationData = new UserRegistrationData(UserRole.USER, "name",
//                "name", "name", "username", "password",
//                Specialization.ART, "description", new ArrayList<>());
//
//
//        mockMvc.perform(
//                    post("/api/registration")
//                        .content(objectMapper.writeValueAsString(userRegistrationData))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    public void testFailRegistrationUser() throws Exception {
//        UserRegistrationData userRegistrationData = new UserRegistrationData(UserRole.USER, "name",
//                "name", "name", "username", "password",
//                Specialization.ART, "description", new ArrayList<>());
//
//
//        mockMvc.perform(
//                post("/api/registration")
//                        .content(objectMapper.writeValueAsString(userRegistrationData))
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isInternalServerError());
//    }
}
