package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.models.Event;
import com.hse.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FeedControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetEmptyArrayOfEvents() throws Exception {
        String token = getToken("username", "password");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("offset", "0");
        map.add("size", "2");
        map.add("specializations", "ART");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/feed/events")
                        .params(map)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Event[] array = objectMapper.readValue(response, Event[].class);
        assertEquals(0, array.length);
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-event.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetNotEmptyArrayOfEvents() throws Exception {
        String token = getToken("username", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/feed/events?offset=0&size=15&specializations=")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Event[] array = objectMapper.readValue(response, Event[].class);
        assertEquals(1, array.length);
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetEmptyArrayOfCreators() throws Exception {
        String token = getToken("username", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/feed/creators?offset=0&size=15&specializations=")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        User[] array = objectMapper.readValue(response, User[].class);
        assertEquals(0, array.length);
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetNotEmptyArrayOfCreators() throws Exception {
        String token = getToken("creator", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/feed/creators?offset=0&size=15&specializations=")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Object[] array = objectMapper.readValue(response, Object[].class);
        assertEquals(1, array.length);
    }
}
