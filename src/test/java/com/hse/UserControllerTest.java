package com.hse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testSaveUser() throws Exception {
//        Assertions.assertNotNull(mockMvc);
//
//        UserRegistrationData userRegistrationData = new UserRegistrationData(
//                1, UserRole.USER, "name", "secondName", "patronymic",
//                "username", "password", Specialization.ART,
//                1, "description", new ArrayList<>());
//
//        mockMvc.perform(
//                post("/users")
//                        .content(new ObjectMapper().writeValueAsString(userRegistrationData))
//                        .contentType(MediaType.APPLICATION_JSON)
//        )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").isNumber())
//                .andExpect(jsonPath("$.username").value("username"));
    }
}
