package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.enums.Specialization;
import com.hse.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
        System.out.println(response);
        assertTrue(Boolean.parseBoolean(response));
    }
}
