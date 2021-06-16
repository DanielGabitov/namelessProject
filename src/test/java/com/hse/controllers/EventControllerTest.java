package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.enums.Specialization;
import com.hse.models.Event;
import com.hse.models.EventRegistrationData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.sql.Timestamp;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateEvent() throws Exception {
        String token = getToken("username", "password");

        EventRegistrationData eventRegistrationData = new EventRegistrationData("name", "desc",
                1, "geo", Specialization.ART, new Timestamp(1), new ArrayList<>());

        mockMvc.perform(
                post("/api/events")
                        .content(objectMapper.writeValueAsString(eventRegistrationData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void updateEvent() throws Exception {
        String token = getToken("username", "password");

        Event event = new Event(1L, "name", "desc", new ArrayList<>(), 1L, new ArrayList<>(),
                "geo", Specialization.ART, new Timestamp(1), new ArrayList<>());

        mockMvc.perform(
                put("/api/events/1")
                        .content(objectMapper.writeValueAsString(event))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetEvent() throws Exception {
        String token = getToken("username", "password");

        mockMvc.perform(
                get("/api/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }
}
