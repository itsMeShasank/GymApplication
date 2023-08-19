package com.epam.gymservice.controller;

import com.epam.gymservice.dto.request.*;
import com.epam.gymservice.dto.response.TrainingDetails;
import com.epam.gymservice.kafka.Producer;
import com.epam.gymservice.restcontroller.TrainingController;
import com.epam.gymservice.service.MailNotificationServiceImpl;
import com.epam.gymservice.service.TraineeServiceImpl;
import com.epam.gymservice.service.TrainerServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrainingController.class)
class TrainingControllerTest {

    @MockBean
    TrainerServiceImpl trainerServiceImpl;
    @MockBean
    TraineeServiceImpl traineeServiceImpl;
    @MockBean
    MailNotificationServiceImpl mailNotificationServiceImpl;
    @MockBean
    Producer producer;
    @Autowired
    MockMvc mockMvc;

    static TrainingDetails trainingDetails;
    static TraineeTrainings traineeTrainings;
    static TrainerTrainings trainerTrainings;
    static Training training;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUpObjectMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Configure JSR310 module
    }

    @BeforeAll
    static void setUp() {
        trainingDetails = new TrainingDetails();
        trainingDetails.setTrainingDate(LocalDate.of(2023, Month.MARCH,29));
        trainingDetails.setTrainingName("yoga");
        trainingDetails.setTrainingType("yoga");
        trainingDetails.setTrainingDuration(20L);
        trainingDetails.setTraineeName("shasank");
        trainingDetails.setTrainerName("hemanth");

        trainerTrainings = new TrainerTrainings();
        trainerTrainings.setTraineeUserName("shasank");
        trainerTrainings.setTrainerUserName("hemanth");
        trainerTrainings.setStartDate(LocalDate.of(2023,Month.AUGUST,20));
        trainerTrainings.setEndDate(LocalDate.of(2023,Month.AUGUST,21));


        training = new Training();
        training.setTraineeUserName("shasnak");
        training.setTrainerUserName("shasank");
        training.setTrainingName("zumba");
        training.setTrainingDuration(20L);
        training.setTrainingDate(LocalDate.of(2023,Month.AUGUST,21));
        training.setTrainingType("zumba");

        traineeTrainings = new TraineeTrainings();
        traineeTrainings.setTraineeUserName("shasank");
        traineeTrainings.setStartDate(LocalDate.of(2023,Month.AUGUST,20));
        traineeTrainings.setEndDate(LocalDate.of(2023,Month.AUGUST,22));
        traineeTrainings.setTrainerName("shasank");
        traineeTrainings.setTrainingType("yoga");

    }

    @Test
    void fetchTraineeTrainingInformationTest() throws Exception{
        Mockito.when(traineeServiceImpl.fetchTraineeTrainingInformation(any(TraineeTrainings.class))).thenReturn(new ArrayList<>(List.of(trainingDetails)));
        mockMvc.perform(post("/gym-service/trainee-trainings-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(traineeTrainings)))
                .andExpect(status().isOk());
    }

    @Test
    void fetchTrainerTrainingsInformationTest() throws Exception{
        Mockito.when(trainerServiceImpl.fetchTrainerTrainingsInformation(any(TrainerTrainings.class))).thenReturn(new ArrayList<>(List.of(trainingDetails)));
        mockMvc.perform(post("/gym-service/trainer-trainings-list")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(trainerTrainings)))
                .andExpect(status().isOk());
    }

    @Test
    void registerTrainingInformationTest() throws Exception {
        Mockito.when(trainerServiceImpl.registerTrainingInformation(any(Training.class))).thenReturn(new TrainingReport());
        Mockito.doNothing().when(producer).sendTrainingReportToServer(new TrainingReport());
        Mockito.when(mailNotificationServiceImpl.prepareMailForTraining(any(Training.class))).thenReturn(new MailDTO());
        Mockito.doNothing().when(producer).sendUserToServer(new MailDTO());

        mockMvc.perform(put("/gym-service/add-training")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(training)))
                .andExpect(status().isOk());
    }
}
