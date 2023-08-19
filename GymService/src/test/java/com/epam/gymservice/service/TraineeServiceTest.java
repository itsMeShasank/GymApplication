package com.epam.gymservice.service;

import com.epam.gymservice.dao.TraineeRepository;
import com.epam.gymservice.dao.TrainingTypeRepository;
import com.epam.gymservice.dao.UserRepository;
import com.epam.gymservice.dto.request.TraineeTrainings;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TraineesTrainerUpdate;
import com.epam.gymservice.dto.response.TraineeDetails;
import com.epam.gymservice.helper.TraineeNotFoundException;
import com.epam.gymservice.helper.UserException;
import com.epam.gymservice.helper.UserNotFoundException;
import com.epam.gymservice.model.*;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class TraineeServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    TraineeRepository traineeRepository;
    @Mock
    TrainingTypeRepository trainingTypeRepository;
    @Mock
    AccountService accountServiceImpl;

    @InjectMocks
    TraineeServiceImpl traineeService;

    static User user;
    static Trainee trainee;
    static Trainer trainer;
    static TraineeUpdate traineeUpdate;
    static TraineeDetails traineeDetails;
    static Training training;
    static  TrainingType trainingType;
    static TraineeTrainings traineeTrainings;
    static TraineesTrainerUpdate traineesTrainerUpdate;

    @BeforeAll
    static void setup() {
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

        trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(new TrainingType());
        trainer.setTrainees(new ArrayList<>(List.of(trainee)));

        trainingType = new TrainingType();
        trainingType.setId(20L);
        trainingType.setSpecialization("Yoga");
        trainingType.setTrainers(new ArrayList<>());
        trainingType.setTraining(new ArrayList<>());

        traineeUpdate = new TraineeUpdate();
        traineeUpdate.setUserName(traineeDetails.getUserName());
        traineeUpdate.setFirstName(traineeDetails.getFirstName());
        traineeUpdate.setLastName(traineeDetails.getLastName());
        traineeUpdate.setMail(traineeDetails.getMail());
        traineeUpdate.setDateOfBirth(traineeDetails.getDateOfBirth());
        traineeUpdate.setAddress(traineeDetails.getAddress());
        traineeUpdate.setActive(true);

        traineesTrainerUpdate = new TraineesTrainerUpdate();
        traineesTrainerUpdate.setTraineeUserName("shasank");
        traineesTrainerUpdate.setTrainersUserName(new ArrayList<>(List.of("shasank")));

        traineeTrainings = new TraineeTrainings();
        traineeTrainings.setTraineeUserName("shasank");
        traineeTrainings.setStartDate(LocalDate.of(2023,Month.MARCH,29));
        traineeTrainings.setEndDate(LocalDate.of(2023,Month.MARCH,31));
        traineeTrainings.setTrainerName("shasank");
        traineeTrainings.setTrainingType(trainingType.getSpecialization());


    }

    @Test
    void fetchTraineeUserExceptionTest() {
        assertThrows(UserException.class,() -> traineeService.fetchTrainee(""));
    }
    @Test
    void fetchTraineeWithTraineeNotFoundExceptionTest() {
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        user.setTrainee(null);
        assertThrows(TraineeNotFoundException.class,() ->traineeService.fetchTrainee("sasi"));
    }

    @Test
    void fetchTraineeTest() {
        trainee.setUser(user);
        user.setUserName("shasank");
        user.setTrainee(trainee);
        user.setTrainer(trainer);
        trainee.setTrainers(new ArrayList<>(List.of(trainer)));

        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(userRepository.findById(any(Long.class))).thenReturn(Optional.ofNullable(user));
        traineeService.fetchTrainee("shasank");
    }

    @Test
    void updateTraineeTest() {
        user.setTrainee(trainee);
        user.setTrainer(trainer);
        trainee.setTrainers(new ArrayList<>(List.of(trainer)));
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(traineeRepository.save(trainee)).thenReturn(trainee);
        Mockito.when(trainingTypeRepository.findById(trainer.getSpecialization().getId())).thenReturn(Optional.of(trainingType));
        traineeService.updateTrainee(traineeUpdate);
    }

    @Test
    void deleteTest() {
        user.setTrainee(trainee);
        user.setTrainer(trainer);
        trainer.setTrainees(new ArrayList<>(List.of(trainee)));
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.doNothing().when(userRepository).delete(user);
        traineeService.deleteTrainee("shasank");
    }

    @Test
    void deleteUserWithTraineeNotFoundExceptionTest() {
        user.setTrainee(null);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        assertThrows(TraineeNotFoundException.class,() ->traineeService.deleteTrainee("shasank"));
    }

    @Test
    void updateTrainerTrainersInformationWithWithUserNotFoundExceptionTest() {
        user.setTrainee(null);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        assertThrows(UserNotFoundException.class,() ->traineeService.updateTraineesTrainersInformation(traineesTrainerUpdate));

    }
    @Test
    void updateTrainerTraineesInformationWithTrainerNotFoundExceptionTest() {
        user.setTrainer(trainer);
        user.setTrainee(trainee);
        trainee.setTrainers(new ArrayList<>(List.of(trainer)));
        trainer.setSpecialization(trainingType);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        traineeService.updateTraineesTrainersInformation(traineesTrainerUpdate);
    }

    @Test
    void fetchTraineeTrainingInformationTest() {
        user.setTrainee(trainee);
        trainee.setTrainers(new ArrayList<>(List.of(trainer)));
        training = new Training();
        training.setId(30L);
        training.setTrainingName("Yoga-Practise");
        training.setTrainingDate(LocalDate.of(2023,Month.MARCH,29));
        training.setTrainingDuration(20L);
        training.setTrainee(trainee);
        training.setTrainer(trainer);
        training.setTrainingType(trainingType);
        List<Training> trainingsList = new ArrayList<>(List.of(training));
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(trainingTypeRepository.findBySpecialization(any(String.class))).thenReturn(Optional.ofNullable(trainingType));
        Mockito.when(traineeRepository.findAllTrainingInBetween(any(LocalDate.class),any(LocalDate.class),any(Trainee.class),any(TrainingType.class))).thenReturn(trainingsList);
        traineeService.fetchTraineeTrainingInformation(traineeTrainings);
    }

}
