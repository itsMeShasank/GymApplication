package com.epam.gymservice.service;

import com.epam.gymservice.dao.TraineeRepository;
import com.epam.gymservice.dao.TrainerRepository;
import com.epam.gymservice.dao.TrainingTypeRepository;
import com.epam.gymservice.dao.UserRepository;
import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.ModifyCredentials;
import com.epam.gymservice.dto.request.TraineeRegistration;
import com.epam.gymservice.dto.request.TrainerRegistration;
import com.epam.gymservice.helper.TrainingNotFoundException;
import com.epam.gymservice.helper.UserException;
import com.epam.gymservice.helper.UserNotFoundException;
import com.epam.gymservice.model.Trainee;
import com.epam.gymservice.model.Trainer;
import com.epam.gymservice.model.TrainingType;
import com.epam.gymservice.model.User;
import com.epam.gymservice.util.CredentialsGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AccountServiceTest {

    @Mock
    TraineeRepository traineeRepository;
    @Mock
    TrainerRepository trainerRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    TrainingTypeRepository trainingTypeRepository;
    @Mock
    CredentialsGenerator credentialsGenerator;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    AccountServiceImpl accountService;

    static CredentialsDetails credentialsDetails;
    static ModifyCredentials modifyCredentials;
    static User user;
    static Trainee trainee;
    static Trainer trainer;
    static  TrainingType trainingType;
    static TraineeRegistration traineeRegistration;
    static TrainerRegistration trainerRegistration;

    @BeforeAll
    static void setUp() {
        credentialsDetails = new CredentialsDetails();
        credentialsDetails.setUserName("shasank");
        credentialsDetails.setPassword("shjg35^4");

        user = new User();
        user.setUserName("shasank");
        user.setPassword("shjg35^4");

        modifyCredentials = new ModifyCredentials();
        modifyCredentials.setOldPassword(user.getPassword());
        modifyCredentials.setNewPassword("fwe345");
        modifyCredentials.setUserName(user.getUserName());

        traineeRegistration = new TraineeRegistration();
        traineeRegistration.setFirstName("shasank");
        traineeRegistration.setLastName("gadipilli");
        traineeRegistration.setMail("shasnak@gmail.com");
        traineeRegistration.setDateOfBirth(LocalDate.of(2002, Month.MARCH,29));;
        traineeRegistration.setAddress("VZM,AP,535002");

        trainee = new Trainee();
        trainee.setUser(user);
        trainee.setDateOfBirth(LocalDate.of(2002, Month.MARCH,29));;
        trainee.setAddress("VZM,AP,535002");

        trainerRegistration = new TrainerRegistration();
        trainerRegistration.setFirstName("shasank");
        trainerRegistration.setLastName("gadipilli");
        trainerRegistration.setMail("shasnak@gmail.com");
        trainerRegistration.setSpecialization("Yoga");

        trainer = new Trainer();
        trainer.setSpecialization(new TrainingType());
        trainer.setUser(user);

        trainingType = new TrainingType();
        trainingType.setSpecialization("Yoga");
        trainingType.setTrainers(new ArrayList<>());
        trainingType.setTraining(new ArrayList<>());
    }

    @Test
    void validateUserNotFoundExceptionRaisingTest() {
        Mockito.when(userRepository.findByUserName(any(String.class))).thenThrow(UserException.class);
        assertThrows(UserException.class,() -> accountService.validateUser(credentialsDetails));
    }
    @Test
    void validateUserPasswordIsNotMatchingTest() {
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.of(user));
        credentialsDetails.setPassword("12345678");
        assertThrows(UserException.class,() -> accountService.validateUser(credentialsDetails));
    }

    @Test
    void validateUserMethodNotProvidingWithCredentialsDetailsTest() {
        credentialsDetails.setUserName("-");
        credentialsDetails.setPassword("-");
        assertThrows(UserException.class,() -> accountService.validateUser(credentialsDetails));
    }
    @Test
    void validateUserWithProperDetailsTest() {
        credentialsDetails.setUserName("shasank");
        credentialsDetails.setPassword("shjg35^4");
        user.setPassword("shjg35^4");
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.of(user));
        assertTrue(accountService.validateUser(credentialsDetails));
    }

    @Test
    void getUserFromDatabaseUserNotFoundExceptionTest() {
        Mockito.when(userRepository.findByUserName(any(String.class))).thenThrow(UserNotFoundException.class);
        assertThrows(UserNotFoundException.class,() -> accountService.getUserFromDatabase("-"));
    }

    @Test
    void getUserFromDatabaseTest() {
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.of(user));
        assertEquals(user,accountService.getUserFromDatabase("shasank"));
    }
    @Test
    void updateUserExceptionThatValidatePasswordTest() {
        modifyCredentials.setOldPassword("sasiSASII");
        modifyCredentials.setNewPassword("SASIIsasi");
        assertThrows(UserException.class,() -> accountService.updateUser(modifyCredentials));
    }

    @Test
    void UpdateUserTest() {
        modifyCredentials.setUserName("shasankGadipilli12");
        modifyCredentials.setNewPassword("@cA$QtN8");
        modifyCredentials.setOldPassword("@cA$Qtq8");
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(user.getPassword())).thenReturn(user.getPassword());
        Mockito.when(userRepository.save(user)).thenReturn(user);
        assertTrue(accountService.updateUser(modifyCredentials));
    }

    @Test
    void updateUserNotFoundExceptionTest() {
        modifyCredentials.setUserName("shasankGadipilli12");
        modifyCredentials.setNewPassword("@cA$QtN8");
        modifyCredentials.setOldPassword("@cA$Qtq8");
        Mockito.when(userRepository.findByUserName(any(String.class))).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,() -> accountService.updateUser(modifyCredentials));
    }

    @Test
    void updateUserExceptionWithEqualsPasswordsDetails() {
        modifyCredentials.setNewPassword("shasank");
        modifyCredentials.setOldPassword("shasank");
        assertThrows(UserException.class, () -> accountService.updateUser(modifyCredentials));
    }

    @Test
    void updateUserExceptionThatOneOfTheFieldIsEmptyTest() {
        modifyCredentials.setUserName("");
        assertThrows(UserException.class,() -> accountService.updateUser(modifyCredentials));
    }

    @Test
    void updateUserWithNotProperPasswordTest() {
        modifyCredentials.setOldPassword("shasank123");
        modifyCredentials.setNewPassword("1234567");
        assertThrows(UserException.class,() -> accountService.updateUser(modifyCredentials));

    }
    @Test
    void updateUserExceptionThatPasswordFieldIsLessThanEightCharactersTest() {
        modifyCredentials.setOldPassword("sasi");
        assertThrows(UserException.class,() -> accountService.updateUser(modifyCredentials));
    }

    @Test
    void saveTraineeTest() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(traineeRepository.save(any(Trainee.class))).thenReturn(trainee);
        assertEquals(new CredentialsDetails(),accountService.saveTrainee(traineeRegistration));
    }

    @Test
    void saveTrainerTest() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        Mockito.when(trainingTypeRepository.findBySpecialization(any(String.class))).thenReturn(Optional.of(trainingType));
        assertEquals(new CredentialsDetails(),accountService.saveTrainer(trainerRegistration));
    }
    @Test
    void saveTrainerExceptionTest() {
        Mockito.when(userRepository.save(any(User.class))).thenReturn(user);
        Mockito.when(trainerRepository.save(any(Trainer.class))).thenReturn(trainer);
        Mockito.when(trainingTypeRepository.findBySpecialization(any(String.class))).thenThrow(TrainingNotFoundException.class);
        assertThrows(TrainingNotFoundException.class,() -> accountService.saveTrainer(trainerRegistration));
    }
}
