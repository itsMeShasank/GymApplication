package com.epam.gymservice.service;

import com.epam.gymservice.dao.TrainerRepository;
import com.epam.gymservice.dao.TrainingRepository;
import com.epam.gymservice.dao.TrainingTypeRepository;
import com.epam.gymservice.dao.UserRepository;
import com.epam.gymservice.dto.request.TrainerTrainings;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.helper.*;
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
class TrainerServiceTest {

    @Mock
    TrainerRepository trainerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TrainingTypeRepository trainingTypeRepository;
    @Mock
    TrainingRepository trainingRepository;
    @Mock
    AccountService accountServiceImpl;

    @InjectMocks
    TrainerServiceImpl trainerService;

    static User user;
    static Trainer trainer;
    static Trainee trainee;
    static Training trainings;
    static com.epam.gymservice.dto.request.Training trainingsDetails;
    static TrainingType trainingType;
    static TrainerUpdate trainerUpdate;
    static TrainerTrainings trainerTrainings;

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

        trainer = new Trainer();
        trainer.setUser(user);
        trainer.setSpecialization(new TrainingType());
        trainer.setTrainees(new ArrayList<>(List.of(trainee)));

        trainerUpdate = new TrainerUpdate();
        trainerUpdate.setUserName("shasank");
        trainerUpdate.setLastName("gadipilli");
        trainerUpdate.setFirstName("shasank");
        trainerUpdate.setMail("shasank@gmail.com");
        trainerUpdate.setSpecialization("Yoga");
        trainerUpdate.setActive(true);

        trainerTrainings = new TrainerTrainings();
        trainerTrainings.setTrainerUserName("shasank");
        trainerTrainings.setTraineeUserName("shasank");
        trainerTrainings.setStartDate(LocalDate.of(2023,Month.AUGUST,20));
        trainerTrainings.setEndDate(LocalDate.of(2023,Month.AUGUST,20));


        trainingType = new TrainingType();
        trainingType.setId(20L);
        trainingType.setSpecialization("Yoga");
        trainingType.setTrainers(new ArrayList<>());


        trainings = new Training();
        trainings.setId(40L);
        trainings.setTrainingName("Yoga-practise");
        trainings.setTrainingDate(LocalDate.of(2023,Month.AUGUST,20));
        trainings.setTrainingDuration(20L);
        trainings.setTrainee(trainee);
        trainings.setTrainer(trainer);
        trainings.setTrainingType(trainingType);

        trainingsDetails = new com.epam.gymservice.dto.request.Training();
        trainingsDetails.setTrainingName("Yoga-practise");
        trainingsDetails.setTrainingDate(LocalDate.of(2023,Month.MARCH,29));
        trainingsDetails.setTrainingDuration(20L);
        trainingsDetails.setTrainingType("Yoga");
        trainingsDetails.setTrainerUserName("shasank");
        trainingsDetails.setTraineeUserName("sasi");
    }

    @Test
    void fetchTrainerWithoutProvidingUserNameTest() {
        assertThrows(UserException.class, () ->trainerService.fetchTrainer(""));
    }

    @Test
    void fetchTrainerWithTrainerNotFoundExceptionTest() {
        user.setTrainer(null);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        assertThrows(TrainerNotFoundException.class,() -> trainerService.fetchTrainer("shasank"));
    }

    @Test
    void fetchTrainerWithProperDetails() {
        user.setTrainer(trainer);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        trainerService.fetchTrainer("shasank");

    }
    @Test
    void updateTrainerWithEmptyUserNameTest() {
        trainerUpdate.setUserName("");
        assertThrows(UserException.class,() -> trainerService.updateTrainer(trainerUpdate));
    }
    @Test
    void updateTrainerWithEmptyFirstNameTest() {
        trainerUpdate.setFirstName("");
        assertThrows(UserException.class,() -> trainerService.updateTrainer(trainerUpdate));
    }

    @Test
    void updateTrainerWithEmptyLastNameTest() {
        trainerUpdate.setLastName("");
        assertThrows(UserException.class,() -> trainerService.updateTrainer(trainerUpdate));
    }

    @Test
    void updateTrainerWithEmptySetSpecializationTest() {
        trainerUpdate.setSpecialization("");
        assertThrows(UserException.class,() -> trainerService.updateTrainer(trainerUpdate));
    }

    @Test
    void updateTrainerDetailsTest() {
        trainerUpdate.setUserName("shasank");
        trainerUpdate.setLastName("gadipilli");
        trainerUpdate.setFirstName("shasank");
        trainerUpdate.setSpecialization("Yoga");
        user.setTrainer(trainer);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(trainerRepository.save(trainer)).thenReturn(trainer);
        trainerService.updateTrainer(trainerUpdate);
    }

    @Test
    void fetchActiveTrainersListWithTraineeNotFoundExceptionTest() {
        user.setTrainee(null);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        assertThrows(TraineeNotFoundException.class,() -> trainerService.fetchActiveTrainer("shasank"));
    }

    @Test
    void fetchActiveTrainersListTest() {
        user.setTrainee(trainee);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(trainerRepository.findTrainersNotInList(any(List.class))).thenReturn(new ArrayList<>(List.of(trainer)));
        trainerService.fetchActiveTrainer("shasank");
    }

    @Test
    void fetchTrainerTrainingsInformationWithTrainerNotFoundExceptionTest() {
        user.setTrainer(null);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        assertThrows(TrainerNotFoundException.class,()->trainerService.fetchTrainerTrainingsInformation(trainerTrainings));
    }


    @Test
    void saveTrainingTypeTest() {
        Mockito.when(trainingTypeRepository.save(any(TrainingType.class))).thenReturn(trainingType);
        trainerService.saveTrainingType("Yoga");
    }

    @Test
    void registerTrainingInformationWithTrainingNotFoundExceptionTest() {

        user.setTrainee(trainee);
        user.setTrainer(trainer);;
        trainer.setSpecialization(trainingType);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(trainingTypeRepository.findBySpecialization("Yoga")).thenReturn(Optional.empty());
        assertThrows(TrainingNotFoundException.class, () -> trainerService.registerTrainingInformation(trainingsDetails));
    }

    @Test
    void registerTrainingInformationWithAssociationExceptionTest() {

        user.setTrainee(trainee);
        user.setTrainer(trainer);
        trainingType.setSpecialization("Zumba");
        trainer.setSpecialization(trainingType);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(trainingTypeRepository.findBySpecialization("Yoga")).thenReturn(Optional.of(trainingType));
        assertThrows(AssociationException.class, () -> trainerService.registerTrainingInformation(trainingsDetails));
    }
    @Test
    void registerTrainingInformationTest() {

        user.setTrainee(trainee);
        user.setTrainer(trainer);;
        trainer.setSpecialization(trainingType);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(accountServiceImpl.getUserFromDatabase(any(String.class))).thenReturn(user);
        Mockito.when(trainingTypeRepository.findBySpecialization("Yoga")).thenReturn(Optional.of(trainingType));
        Mockito.when(trainingRepository.save(trainings)).thenReturn(trainings);
        trainerService.registerTrainingInformation(trainingsDetails);
    }

    @Test
    void fetchTrainerTrainingsInformationTest() {

        Trainee trainee1 = new Trainee();
        Trainer trainer1 = new Trainer();
        Training training1 = new Training();

        User user1 = new User();
        user1.setId(111L);
        user1.setUserName("shasank");
        user1.setPassword("shjg35^4");
        user1.setFirstName("shasank");
        user1.setLastName("gadipilli");
        user1.setMail("shasank@gmail.com");
        user1.setActive(true);


        User user2 = new User();
        user2.setId(112L);
        user1.setUserName("charan");
        user1.setPassword("shjg35^4");
        user1.setFirstName("charan");
        user1.setLastName("gadipilli");
        user1.setMail("charan@gmail.com");
        user1.setActive(true);


        trainee1.setId(114L);
        user2.setTrainee(trainee1);
        trainee1.setUser(user2);
        trainee1.setDateOfBirth(LocalDate.of(2002,3,29));
        trainee1.setAddress("VZM");
        trainee1.setTraining(new ArrayList<>(List.of(training1)));
        trainee1.setTrainers(new ArrayList<>(List.of(trainer1)));


        trainer1.setId(113L);
        user1.setTrainer(trainer1);
        trainer1.setUser(user1);
        trainer1.setSpecialization(trainingType);
        trainer1.setTraining(new ArrayList<>(List.of(training1)));
        trainer1.setTrainees(new ArrayList<>(List.of(trainee1)));

        training1.setId(222L);
        training1.setTrainingType(trainingType);
        training1.setTrainingName("Yoga-practise");
        training1.setTrainingDuration(20L);
        training1.setTrainingDate(LocalDate.of(2023,Month.AUGUST,21));
        training1.setTrainer(trainer1);
        training1.setTrainee(trainee1);

        TrainerTrainings trainerTrainings1 = new TrainerTrainings();
        trainerTrainings1.setTrainerUserName("shasank");
        trainerTrainings1.setTraineeUserName("charan");
        trainerTrainings1.setStartDate(training1.getTrainingDate());
        trainerTrainings1.setEndDate(training1.getTrainingDate());

        Mockito.when(accountServiceImpl.getUserFromDatabase("shasank")).thenReturn(user1);
        Mockito.when(accountServiceImpl.getUserFromDatabase("charan")).thenReturn(user2);
        Mockito.when(trainerRepository.findAllTrainingInBetween(any(LocalDate.class),any(LocalDate.class),any(Trainer.class),any(Trainee.class))).thenReturn(new ArrayList<>(List.of(training1)));
        trainerService.fetchTrainerTrainingsInformation(trainerTrainings1);


    }

}
