package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.models.Application;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrganizerControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql",
            "/scripts/create-organizer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testInviteCreator() throws Exception {
        String token = getToken("organizer", "password");

        mockMvc.perform(
                post("/api/organizers/3/invites/2")
                        .param("eventId", "1")
                        .content("application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql",
            "/scripts/create-organizer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAnswerApplication() throws Exception {
        String token = getToken("creator", "password");

        mockMvc.perform(
                post("/api/creators/2/applications/1")
                        .content("application")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        token = getToken("organizer", "password");
        mockMvc.perform(
                post("/api/organizers/3/events/1/applications/2")
                        .param("acceptance", "false")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql",
            "/scripts/create-organizer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetOrganizerApplications() throws Exception {
        testInviteCreator();

        String token = getToken("organizer", "password");
        MvcResult mvcResult = mockMvc.perform(
                get("/api/organizers/3/applications")
                        .param("acceptance", "false")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<Application> eventList = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Application.class));
        assertEquals(1, eventList.size());
    }
}
