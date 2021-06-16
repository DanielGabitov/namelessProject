package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SubscriptionControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-user1.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testAddSubscription() throws Exception {
        String token = getToken("username", "password");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", "1");
        map.add("subscriptionId", "2");

        mockMvc.perform(
                post("/api/subscriptions")
                        .params(map)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-user1.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testDeleteSubscription() throws Exception {
        testAddSubscription();

        String token = getToken("username", "password");

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("userId", "1");
        map.add("subscriptionId", "2");

        mockMvc.perform(
                delete("/api/subscriptions")
                        .params(map)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-user1.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetAllSubscriptions() throws Exception {
        testAddSubscription();

        String token = getToken("username", "password");

        mockMvc.perform(
                get("/api/subscriptions")
                        .param("userId", "1")
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }
}
