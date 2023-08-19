package com.epam.gymservice.service;

import com.epam.gymservice.dao.UserRepository;
import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.dto.request.Training;
import com.epam.gymservice.dto.response.TraineeDetails;
import com.epam.gymservice.helper.UserNotFoundException;
import com.epam.gymservice.model.Trainee;
import com.epam.gymservice.model.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MailNotificationServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    MailNotificationServiceImpl notificationService;

    CredentialsDetails credentialsDetails;
    static User user;
    static TraineeDetails traineeDetails;
    static Trainee trainee;
    static TraineeUpdate traineeUpdate;
    static Training training;

    @BeforeAll
    static void setUp() {
        user = new User();
        user.setId(10L);
        user.setUserName("shasank");
        user.setPassword("shjg35^4");
        user.setFirstName("shasank");
        user.setLastName("gadipilli");
        user.setMail("shasank@gmail.com");
        user.setActive(true);

        trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(LocalDate.of(2002, Month.MARCH,29));
        trainee.setAddress("VZM,AP,535002");
        trainee.setTrainers(new ArrayList<>());

        traineeDetails = new TraineeDetails();
        traineeDetails.setUserName(user.getUserName());
        traineeDetails.setFirstName(user.getFirstName());
        traineeDetails.setLastName(user.getLastName());
        traineeDetails.setMail(user.getMail());
        traineeDetails.setDateOfBirth(trainee.getDateOfBirth());
        traineeDetails.setActive(true);
        traineeDetails.setTrainersList(new ArrayList<>());
        traineeDetails.setAddress(trainee.getAddress());

        traineeUpdate = new TraineeUpdate();
        traineeUpdate.setUserName(traineeDetails.getUserName());
        traineeUpdate.setFirstName(traineeDetails.getFirstName());
        traineeUpdate.setLastName(traineeDetails.getLastName());
        traineeUpdate.setMail(traineeDetails.getMail());
        traineeUpdate.setDateOfBirth(traineeDetails.getDateOfBirth());
        traineeUpdate.setAddress(traineeDetails.getAddress());
        traineeUpdate.setActive(true);

        training = new Training();
        training.setTraineeUserName(trainee.getUser().getUserName());
        training.setTrainerUserName("shasank");
        training.setTrainingName("Yoga-practise");
        training.setTrainingDate(LocalDate.of(2023,Month.MARCH,31));
        training.setTrainingType("Yoga");
        training.setTrainingDuration(30L);
    }

    @Test
    void prepareMailForCredentialsTest() {
        credentialsDetails = new CredentialsDetails("shasank","sasi123");
        notificationService.prepareMailForCredentials(credentialsDetails,"shasank@gmail.com");
    }

    @Test
    void prepareMailForTraineeTest() {


        notificationService.prepareMailForTrainee("shasank@gmail.com",traineeUpdate);
    }

    @Test
    void prepareMailForTrainerTest() {

        TrainerUpdate trainerUpdate = new TrainerUpdate();
        trainerUpdate.setUserName("shasank");
        trainerUpdate.setFirstName("shasank");
        trainerUpdate.setLastName("gadipilli");
        trainerUpdate.setMail("shasank@gmail.com");
        trainerUpdate.setSpecialization("Yoga");
        trainerUpdate.setActive(true);
        notificationService.prepareMailForTrainer("shasank@gmail.com",trainerUpdate);

    }

    @Test
    void prepareMailForTrainingTest() {
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.ofNullable(user));
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.ofNullable(user));
        notificationService.prepareMailForTraining(training);
    }

    @Test
    void PrepareMailForTrainingWithUserNotFoundExceptionTest() {
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,() -> notificationService.prepareMailForTraining(training));
    }


}
