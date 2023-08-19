package com.epam.gymservice.controller;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TraineeRegistration;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TraineesTrainerUpdate;
import com.epam.gymservice.dto.response.TraineeDetails;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.kafka.Producer;
import com.epam.gymservice.restcontroller.TraineeController;
import com.epam.gymservice.service.AccountServiceImpl;
import com.epam.gymservice.service.MailNotificationServiceImpl;
import com.epam.gymservice.service.TraineeServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(TraineeController.class)
class TraineeControllerTest {

    @MockBean
    TraineeServiceImpl traineeServiceImpl;
    @MockBean
    AccountServiceImpl accountServiceImpl;
    @MockBean
    MailNotificationServiceImpl mailNotificationServiceImpl;
    @MockBean
    Producer producer;
    @Autowired
    MockMvc mockMvc;

    static TraineeRegistration traineeRegistration;
    static CredentialsDetails credentialsDetails;
    static TraineeDetails traineeDetails;
    static TraineeUpdate traineeUpdate;
    static TrainerDetails trainerDetails;
    static TraineesTrainerUpdate traineesTrainerUpdate;
    static MailDTO mailDTO;
    static ObjectMapper objectMapper;
    @BeforeAll
    static void setUp() {

        objectMapper = new ObjectMapper();

        credentialsDetails = new CredentialsDetails();
        credentialsDetails.setUserName("shasank");
        credentialsDetails.setPassword("shasank123");

        traineeRegistration = new TraineeRegistration();
        traineeRegistration.setFirstName("shasnak");;
        traineeRegistration.setLastName("shasank143");
        traineeRegistration.setMail("shasank@gmail.com");
        traineeRegistration.setAddress("VZM,AP,535002");

        mailDTO = MailDTO.builder()
                .recipients(new ArrayList<>(List.of("shasank@gmail.com")))
                .message(new HashMap<>())
                .emailType("TRAINEE PROFILE UPDATED")
                .build();

        traineeDetails = new TraineeDetails();
        traineeDetails.setUserName("shasank");
        traineeDetails.setFirstName("shasank");
        traineeDetails.setLastName("gadipilli");
        traineeDetails.setActive(true);
        traineeDetails.setAddress("VZM,AP,535002");
        traineeDetails.setDateOfBirth(LocalDate.of(2023, Month.MARCH,29));

        trainerDetails = new TrainerDetails();
        trainerDetails.setUserName("shasank");
        trainerDetails.setFirstName("shasank");
        trainerDetails.setLastName("gadipilli");
        trainerDetails.setActive(true);

        traineeUpdate = new TraineeUpdate();
        traineeUpdate.setUserName("shasank");
        traineeUpdate.setFirstName("shasank");
        traineeUpdate.setLastName("gadipilli");
        traineeUpdate.setAddress("VZM");
        traineeUpdate.setDateOfBirth(LocalDate.of(2002, 3,29));

        traineesTrainerUpdate = new TraineesTrainerUpdate();
        traineesTrainerUpdate.setTraineeUserName("shasank");
        traineesTrainerUpdate.setTrainersUserName(new ArrayList<>(List.of("shasank")));
    }

    @Test
    void registerTraineeDetailsTest() throws Exception {
        Mockito.when(accountServiceImpl.saveTrainee(any(TraineeRegistration.class))).thenReturn(credentialsDetails);
        Mockito.when(mailNotificationServiceImpl.prepareMailForCredentials(any(CredentialsDetails.class),any(String.class))).thenReturn(mailDTO);
        Mockito.doNothing().when(producer).sendUserToServer(mailDTO);

        mockMvc.perform(post("/gym-service/trainee/trainee-signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(traineeRegistration)))
                .andExpect(status().isOk());

    }
    @Test
    void registerTraineeDetailsWithMethodArgumentNotValidExceptionTest() throws Exception {
        mockMvc.perform(post("/gym-service/trainee/trainee-signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new TraineeRegistration())))
                .andExpect(status().isBadRequest());

    }


    @Test
    void fetchTraineeInformationTest() throws Exception {
        Mockito.when(traineeServiceImpl.fetchTrainee(any(String.class))).thenReturn(traineeDetails);

        mockMvc.perform(get("/gym-service/trainee/fetch-details/shasank"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTraineeInformationTest() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        Mockito.when(traineeServiceImpl.updateTrainee(any(TraineeUpdate.class))).thenReturn(traineeDetails);
        Mockito.when(mailNotificationServiceImpl.prepareMailForTrainee(any(String.class),any(TraineeUpdate.class))).thenReturn(mailDTO);
        Mockito.doNothing().when(producer).sendUserToServer(mailDTO);

        mockMvc.perform(put("/gym-service/trainee/update-trainee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(traineeUpdate)))
                .andExpect(status().isOk());

    }

    @Test
    void deleteTraineeTest() throws Exception {
        Mockito.doNothing().when(traineeServiceImpl).deleteTrainee(any(String.class));
        mockMvc.perform(delete("/gym-service/trainee/delete-trainee/shasank"))
                .andExpect(status().isOk());
    }

    @Test
    void updateTraineesTrainersInformationTest() throws Exception {
        Mockito.when(traineeServiceImpl.updateTraineesTrainersInformation(any(TraineesTrainerUpdate.class))).thenReturn(List.of(trainerDetails));

        mockMvc.perform(put("/gym-service/trainee/update-trainees-trainers-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(traineesTrainerUpdate)))
                .andExpect(status().isOk());
    }

}
