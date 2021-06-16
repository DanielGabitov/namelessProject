package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthenticationControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAuthentication() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("username", List.of("username"));
        map.put("password", List.of("password"));

        mockMvc.perform(
                post("/api/authentication")
                        .params(map)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .containsHeader(HttpHeaders.AUTHORIZATION);
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testFailAuthentication() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("username", List.of("username"));
        map.put("password", List.of("password"));

        mockMvc.perform(
                post("/api/authentication")
                        .params(map)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION));
    }
}
