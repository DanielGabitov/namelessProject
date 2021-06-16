package com.hse.controllers;

import com.hse.AbstractIntegrationTest;
import com.hse.models.CreativeAssociation;
import com.hse.models.CreativeAssociationRegistrationData;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CreativeAssociationControllerTest extends AbstractIntegrationTest {

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testCreateCreativeAssociation() throws Exception {
        String token = getToken("creator", "password");

        CreativeAssociationRegistrationData data = new CreativeAssociationRegistrationData("creative association",
                "desc", 2L, new ArrayList<>());

        mockMvc.perform(
                post("/api/creativeAssociation")
                        .content(objectMapper.writeValueAsString(data))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-creative-association.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testGetCreativeAssociation() throws Exception {
        String token = getToken("creator", "password");

        MvcResult mvcResult = mockMvc.perform(
                get("/api/creativeAssociation/{associationId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        CreativeAssociation association = objectMapper.readValue(response, CreativeAssociation.class);
        assert association != null;
    }

    @Test
    @Sql(value = {"/scripts/before-test.sql", "/scripts/create-creator.sql", "/scripts/create-creative-association.sql",
            "/scripts/create-creator-2.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void testInviteCreator() throws Exception {
        String token = getToken("creator", "password");

        mockMvc.perform(
                post("/api/creativeAssociation/{associationId}/members/{creatorToInviteId}", 1, 1002)
                        .header(HttpHeaders.AUTHORIZATION, token))
                .andExpect(status().isOk());
    }
}
