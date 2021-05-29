package com.hse.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class SubscriptionControllerTest {
    private final MockMvc mockMvc;

    @Autowired
    public SubscriptionControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

//    @Test
//    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-user1.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    public void testAddSubscription() throws Exception {
//        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.add("username", "username");
//        multiValueMap.add("password", "password");
//
//        String token = mockMvc.perform(
//                post("/api/authentication")
//                        .params(multiValueMap))
//                .andReturn()
//                .getResponse()
//                .getHeader(HttpHeaders.AUTHORIZATION);
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("userId", "1");
//        map.add("subscriptionId", "2");
//
//        mockMvc.perform(
//                post("/api/subscriptions")
//                        .params(map)
//                        .header(HttpHeaders.AUTHORIZATION, token))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-user1.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    public void testDeleteSubscription() throws Exception {
//        testAddSubscription();
//
//        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.add("username", "username");
//        multiValueMap.add("password", "password");
//
//        String token = mockMvc.perform(
//                post("/api/authentication")
//                        .params(multiValueMap))
//                .andReturn()
//                .getResponse()
//                .getHeader(HttpHeaders.AUTHORIZATION);
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("userId", "1");
//        map.add("subscriptionId", "2");
//
//        mockMvc.perform(
//                delete("/api/subscriptions")
//                        .params(map)
//                        .header(HttpHeaders.AUTHORIZATION, token))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql", "/scripts/create-user1.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    public void testGetAllSubscriptions() throws Exception {
//        testAddSubscription();
//
//        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
//        multiValueMap.add("username", "username");
//        multiValueMap.add("password", "password");
//
//        String token = mockMvc.perform(
//                post("/api/authentication")
//                        .params(multiValueMap))
//                .andReturn()
//                .getResponse()
//                .getHeader(HttpHeaders.AUTHORIZATION);
//
//        mockMvc.perform(
//                get("/api/subscriptions")
//                .param("userId", "1")
//                .header(HttpHeaders.AUTHORIZATION, token))
//                .andExpect(status().isOk());
//    }
}
