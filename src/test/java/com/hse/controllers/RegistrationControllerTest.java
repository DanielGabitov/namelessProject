package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import com.hse.models.UserRegistrationData;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRegistrationUser() throws Exception {
        UserRegistrationData userRegistrationData = new UserRegistrationData(UserRole.USER, "name",
                "name", "name", "username", "password",
                Specialization.ART, "description", new ArrayList<>());

        mockMvc.perform(
                post("/api/registration")
                        .content(objectMapper.writeValueAsString(userRegistrationData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFailRegistrationUser() throws Exception {
        UserRegistrationData userRegistrationData = new UserRegistrationData(UserRole.USER, "name",
                "name", "name", "username", "password",
                Specialization.ART, "description", new ArrayList<>());

        mockMvc.perform(
                post("/api/registration")
                        .content(objectMapper.writeValueAsString(userRegistrationData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }
}
