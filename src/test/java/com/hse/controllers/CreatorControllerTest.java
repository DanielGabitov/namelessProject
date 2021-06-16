package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.models.Invitation;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreatorControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testSendApplication() throws Exception {
        String token = getToken("creator", "password");

        mockMvc.perform(
                post("/api/creators/2/applications/1")
                        .content("application")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCheckIfCreatorSentApplicationToEvent() throws Exception {
        String token = getToken("creator", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/creators/2/applications/1")
                        .content("application")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertFalse(Boolean.parseBoolean(response));

        testSendApplication();

        mvcResult = mockMvc.perform(
                get("/api/creators/2/applications/1")
                        .content("application")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql",
            "/scripts/create-organizer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAnswerInvitation() throws Exception {
        String token = getToken("organizer", "password");

        mockMvc.perform(
                post("/api/organizers/{organizerId}/invites/{creatorId}", 3, 2)
                        .param("eventId", "1")
                        .content("invite creator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        token = getToken("creator", "password");
        mockMvc.perform(
                post("/api/creators/2/invites/1")
                        .param("acceptance", "false")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql",
            "/scripts/create-organizer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetCreatorInvitations() throws Exception {
        String token = getToken("creator", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/creators/2/invitations")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<Invitation> invitations = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Invitation.class));
        assertTrue(invitations.isEmpty());

        token = getToken("organizer", "password");

        mockMvc.perform(
                post("/api/organizers/{organizerId}/invites/{creatorId}", 3, 2)
                        .param("eventId", "1")
                        .content("invite creator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        token = getToken("creator", "password");

        mvcResult = mockMvc.perform(
                get("/api/creators/2/invitations")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        invitations = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Invitation.class));
        assertEquals(1, invitations.size());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-event.sql",
            "/scripts/create-organizer.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCheckIfCreatorHasInvitationToEvent() throws Exception {
        String token = getToken("creator", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/creators/2/invitations/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertFalse(Boolean.parseBoolean(response));

        token = getToken("organizer", "password");

        mockMvc.perform(
                post("/api/organizers/{organizerId}/invites/{creatorId}", 3, 2)
                        .param("eventId", "1")
                        .content("invite creator")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        token = getToken("creator", "password");

        mvcResult = mockMvc.perform(
                get("/api/creators/2/invitations/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        assertTrue(Boolean.parseBoolean(response));
    }
}
