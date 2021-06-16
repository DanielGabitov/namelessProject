package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import com.hse.models.Event;
import com.hse.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testUpdateUser() throws Exception {
        String token = getToken("username", "password");

        Map<String, Object> map = new HashMap<>();
        map.put("id", 1);
        map.put("userRole", UserRole.CREATOR);
        map.put("firstname", "name");
        map.put("lastname", "name");
        map.put("patronymic", "name");
        map.put("username", "username");
        map.put("password", "password");
        map.put("specialization", Specialization.ART);
        map.put("rating", 1);
        map.put("description", "desc");
        map.put("images", new ArrayList<>());

        mockMvc.perform(
                put("/api/users/1")
                        .content(objectMapper.writeValueAsString(map))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetUser() throws Exception {
        String token = getToken("username", "password");

        mockMvc.perform(
                get("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAddLike() throws Exception {
        String token = getToken("username", "password");

        mockMvc.perform(
                post("/api/users/1/likes/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testReLike() throws Exception {
        testAddLike();

        String token = getToken("username", "password");

        mockMvc.perform(
                post("/api/users/1/likes/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetLikes() throws Exception {
        testAddLike();

        String token = getToken("username", "password");

        mockMvc.perform(
                get("/api/users/1/likes")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCheckIfLikeExists() throws Exception {
        String token = getToken("username", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/users/1/likes/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertFalse(Boolean.parseBoolean(response));

        testAddLike();

        mvcResult = mockMvc.perform(
                get("/api/users/1/likes/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testRemoveLike() throws Exception {
        testAddLike();

        String token = getToken("username", "password");

        mockMvc.perform(
                delete("/api/users/1/likes/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
                get("/api/users/1/likes/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertFalse(Boolean.parseBoolean(response));
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testParticipate() throws Exception {
        String token = getToken("username", "password");

        mockMvc.perform(
                post("/api/users/1/events/1/participants")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCheckIfParticipationExists() throws Exception {
        String token = getToken("username", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/users/1/participations/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertFalse(Boolean.parseBoolean(response));

        testParticipate();

        mvcResult = mockMvc.perform(
                get("/api/users/1/participations/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        assertTrue(Boolean.parseBoolean(response));
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCancelParticipation() throws Exception {
        testParticipate();

        String token = getToken("username", "password");

        mockMvc.perform(
                delete("/api/users/1/events/1/participants")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(
                get("/api/users/1/participations/1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        assertFalse(Boolean.parseBoolean(response));
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetUserParticipations() throws Exception {
        String token = getToken("username", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/users/1/participations")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<Event> events = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class));
        assertTrue(events.isEmpty());

        testParticipate();

        mvcResult = mockMvc.perform(
                get("/api/users/1/participations")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        response = mvcResult.getResponse().getContentAsString();
        events = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, Event.class));
        assertEquals(1, events.size());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-users.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testSearchUsers() throws Exception {
        String token = getToken("username", "password");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("username", "second");
        map.add("offset", "0");
        map.add("size", "1");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/users/search")
                        .params(map)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        List<User> users = objectMapper.readValue(response,
                objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        assertEquals(1, users.size());
        assertEquals("second", users.get(0).getUsername());
    }
}
