package com.epam.gymservice.controller;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.ModifyCredentials;
import com.epam.gymservice.restcontroller.AccountController;
import com.epam.gymservice.service.AccountServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AuthControllerTest {

    @MockBean
    AccountServiceImpl accountService;


    @Autowired
    MockMvc mockMvc;

    static CredentialsDetails credentialsDetails;
    static ModifyCredentials modifyCredentials;

    @BeforeAll
    static void setUp() {
        credentialsDetails = new CredentialsDetails();
        credentialsDetails.setUserName("shasank");
        credentialsDetails.setPassword("shasank123");

        modifyCredentials = new ModifyCredentials();
        modifyCredentials.setUserName(credentialsDetails.getUserName());
        modifyCredentials.setOldPassword(credentialsDetails.getPassword());
        modifyCredentials.setNewPassword("shasank143");
    }

    @Test
    void userLoginAPITest() throws Exception {
        Mockito.when(accountService.validateUser(credentialsDetails)).thenReturn(true);

        mockMvc.perform(post("/gym-service/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(credentialsDetails)))
                .andExpect(status().isOk());
    }

    @Test
    void userLoginWithMethodArgumentNotValidExceptionTest() throws Exception {

        mockMvc.perform(post("/gym-service/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new CredentialsDetails())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePasswordTest() throws Exception {
        mockMvc.perform(put("/gym-service/update-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(modifyCredentials)))
                .andExpect(status().isOk());
    }

    @Test
    void updatePasswordWithMethodArgumentNotValidExceptionTest() throws Exception {
        Mockito.when(accountService.updateUser(modifyCredentials)).thenReturn(false);
        mockMvc.perform(put("/gym-service/update-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new ModifyCredentials())))
                .andExpect(status().isBadRequest());
    }
}
