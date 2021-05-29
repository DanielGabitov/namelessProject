package com.hse.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserControllerTest {
    private final MockMvc mockMvc;

    @Autowired
    public UserControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

//    @Test
//    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-user.sql"},
//            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    public void test() throws Exception {
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.put("username", List.of("username"));
//        map.put("password", List.of("password"));
//
//        mockMvc.perform(
//                post("/api/authentication")
//                        .params(map)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .containsHeader(HttpHeaders.AUTHORIZATION);
//    }
//
//    @Test
//    @Sql(value = {"/scripts/before-test.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//    public void testFailAuthentication() throws Exception {
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.put("username", List.of("username"));
//        map.put("password", List.of("password"));
//
//        mockMvc.perform(
//                post("/api/authentication")
//                        .params(map)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden())
//                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION));
//    }
}
