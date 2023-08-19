package com.epam.gymservice.controller;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TrainerRegistration;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.kafka.Producer;
import com.epam.gymservice.model.Trainer;
import com.epam.gymservice.restcontroller.TrainerController;
import com.epam.gymservice.service.AccountServiceImpl;
import com.epam.gymservice.service.MailNotificationServiceImpl;
import com.epam.gymservice.service.TrainerServiceImpl;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainerController.class)
class TrainerControllerTest {

    @MockBean
    TrainerServiceImpl trainerServiceImpl;
    @MockBean
    AccountServiceImpl accountServiceImpl;
    @MockBean
    MailNotificationServiceImpl mailNotificationServiceImpl;
    @MockBean
    Producer producer;
    @Autowired
    MockMvc mockMvc;

    static MailDTO mailDTO;
    static Trainer trainer;
    static CredentialsDetails credentialsDetails;
    static TrainerRegistration trainerRegistration;
    static TrainerDetails trainerDetails;
    static TrainerUpdate trainerUpdate;

    @BeforeAll
    static void setUp() {
        credentialsDetails = new CredentialsDetails();
        credentialsDetails.setUserName("shasank");
        credentialsDetails.setPassword("shasank123");

        trainerRegistration = new TrainerRegistration();
        trainerRegistration.setFirstName("shasank");;
        trainerRegistration.setLastName("shasank143");
        trainerRegistration.setMail("shasank@gmail.com");
        trainerRegistration.setSpecialization("zumba");

        mailDTO = MailDTO.builder()
                .recipients(new ArrayList<>(List.of("shasank@gmail.com")))
                .message(new HashMap<>())
                .emailType("TRAINEE PROFILE UPDATED")
                .build();

        trainerDetails = new TrainerDetails();
        trainerDetails.setUserName("shasank");
        trainerDetails.setFirstName("shasank");
        trainerDetails.setLastName("gadipilli");
        trainerDetails.setActive(true);

        trainerUpdate  = new TrainerUpdate();
        trainerUpdate.setUserName("shasank");
        trainerUpdate.setFirstName("shasnak");
        trainerUpdate.setLastName("gadipilli");
        trainerUpdate.setMail("shasank@gmail.com");
        trainerUpdate.setSpecialization("zumba");
        trainerUpdate.setActive(true);

        trainer = new Trainer();
        trainer.setId(2L);
        trainer.setTraining(new ArrayList<>());
    }

    @Test
    void registerTrainerTest() throws Exception {

        Mockito.when(accountServiceImpl.saveTrainer(any(TrainerRegistration.class))).thenReturn(credentialsDetails);
        Mockito.when(mailNotificationServiceImpl.prepareMailForCredentials(any(CredentialsDetails.class),any(String.class))).thenReturn(mailDTO);
        Mockito.doNothing().when(producer).sendUserToServer(mailDTO);

        mockMvc.perform(post("/gym-service/trainer/trainer-signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(trainerRegistration)))
                .andExpect(status().isOk());
    }

    @Test
    void fetchTrainerTest() throws Exception {

        Mockito.when(trainerServiceImpl.fetchTrainer(any(String.class))).thenReturn(trainerDetails);

        mockMvc.perform(get("/gym-service/trainer/fetch-trainer/shasank"))
                .andExpect(status().isOk());
    }

    @Test
    void trainerUpdateTest() throws Exception{
        Mockito.when(trainerServiceImpl.updateTrainer(any(TrainerUpdate.class))).thenReturn(trainerDetails);
        Mockito.when(mailNotificationServiceImpl.prepareMailForTrainer("shasank@gmail.com",trainerUpdate)).thenReturn(mailDTO);
        Mockito.doNothing().when(producer).sendUserToServer(mailDTO);
        mockMvc.perform(put("/gym-service/trainer/update-trainer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(trainerUpdate)))
                .andExpect(status().isOk());
    }

    @Test
    void fetchActiveTrainersNotAssignedOnTraineeTest() throws Exception {
        Mockito.when(trainerServiceImpl.fetchActiveTrainer(any(String.class))).thenReturn(new ArrayList<>(List.of(trainerDetails)));
        mockMvc.perform(get("/gym-service/trainer/active-trainers/shasank"))
                .andExpect(status().isOk());
    }


}
