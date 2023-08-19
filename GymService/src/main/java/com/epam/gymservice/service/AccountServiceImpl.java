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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final TraineeRepository traineeRepository;
    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final CredentialsGenerator credentialsGenerator;
    private final PasswordEncoder passwordEncoder;


    @Override
    public boolean validateUser(CredentialsDetails details) {
        log.info(SERVICE_METHOD_ENTERED,"validateUser");
        if (!validateCredentials(details)) {
            throw new UserException("Please provide proper username and password to login", HttpStatus.BAD_REQUEST);
        }
        User user = getUserFromDatabase(details.getUserName());
        if (isPasswordSame(user,details)) {
            throw new UserException("Password doesn't matching,\nPlease re-enter you're password", HttpStatus.BAD_REQUEST);
        }
        log.info(SERVICE_METHOD_EXITED,"validateUser");
        return true;
    }

    @Override
    public User getUserFromDatabase(String userName) {
        log.info(SERVICE_METHOD_ENTERED,"getUserFromDatabase");
        User userFound =  userRepository.findByUserName(userName)
                .orElseThrow(() -> new UserNotFoundException("No username found with mention details", HttpStatus.BAD_REQUEST));
        log.info(SERVICE_METHOD_EXITED,"getUserFromDatabase");
        return userFound;
    }

    private boolean isPasswordSame(User user,CredentialsDetails details) {
        log.info(SERVICE_METHOD_ENTERED,"isPasswordSame");
        return !user.getPassword().equals(details.getPassword());
    }
    @Override
    public boolean updateUser(ModifyCredentials details) {
        log.info(SERVICE_METHOD_ENTERED,"updateUser");
        if (!validateCredentials(details)) {
            throw new UserException("Please provide proper username and password to login", HttpStatus.BAD_REQUEST);
        }
        User user = getUserFromDatabase(details.getUserName());
        
        user.setPassword(passwordEncoder.encode(details.getNewPassword()));
        userRepository.save(user);
        log.info(SERVICE_METHOD_EXITED,"updateUser");
        return true;
    }

    @Override
    public CredentialsDetails saveTrainee(TraineeRegistration traineeDetails) {
        log.info(SERVICE_METHOD_ENTERED,"saveTrainee");
        CredentialsDetails credentials = getCredentials(traineeDetails.getFirstName(), traineeDetails.getLastName());
        User userDetails = createUserFromCredentials(credentials,traineeDetails.getFirstName(),traineeDetails.getLastName(),traineeDetails.getMail());

        Trainee trainee = new Trainee();
        trainee.setDateOfBirth(traineeDetails.getDateOfBirth());
        trainee.setAddress(traineeDetails.getAddress());
        trainee.setUser(userDetails);

        userRepository.save(userDetails);
        traineeRepository.save(trainee);
        log.info(SERVICE_METHOD_EXITED,"saveTrainee");
        return credentials;
    }

    @Override
    public CredentialsDetails saveTrainer(TrainerRegistration trainerDetails) {
        log.info(SERVICE_METHOD_ENTERED,"saveTrainer");
        CredentialsDetails credentials = getCredentials(trainerDetails.getFirstName(), trainerDetails.getLastName());
        User userDetails = createUserFromCredentials(credentials,trainerDetails.getFirstName(),trainerDetails.getLastName(),trainerDetails.getMail());

        TrainingType trainingTypeDetails = trainingTypeRepository.findBySpecialization(trainerDetails.getSpecialization())
                .orElseThrow(() -> new TrainingNotFoundException("No Training Type found with given Training type", HttpStatus.BAD_REQUEST));

        Trainer trainer = new Trainer();
        trainer.setSpecialization(trainingTypeDetails);
        trainer.setUser(userDetails);

        userRepository.save(userDetails);
        trainerRepository.save(trainer);
        log.info(SERVICE_METHOD_EXITED,"saveTrainer");
        return credentials;
    }
    private User createUserFromCredentials(CredentialsDetails credentials, String firstName, String lastName, String mail) {
        log.info(SERVICE_METHOD_ENTERED,"createUserFromCredentials");
        User userDetails = new User();
        userDetails.setUserName(credentials.getUserName());
        userDetails.setPassword(passwordEncoder.encode(credentials.getPassword()));
        userDetails.setFirstName(firstName);
        userDetails.setLastName(lastName);
        userDetails.setMail(mail);
        userDetails.setActive(true);
        log.info(SERVICE_METHOD_EXITED,"createUserFromCredentials");
        return userDetails;
    }

    private CredentialsDetails getCredentials(String firstName, String lastName) {
        log.info(SERVICE_METHOD_ENTERED,"getCredentials");
        String username = credentialsGenerator.generateUserName(firstName, lastName);
        String password = credentialsGenerator.generatePassword();
        CredentialsDetails credentialsDetails = new CredentialsDetails();
        credentialsDetails.setUserName(username);
        credentialsDetails.setPassword(password);
        log.info(SERVICE_METHOD_EXITED,"getCredentials");
        return credentialsDetails;
    }

    private boolean validateCredentials(CredentialsDetails details) {
        log.info(SERVICE_METHOD_ENTERED,VALIDATE_CREDENTIALS);
        return details.getUserName().trim().length() != 0 && details.getPassword().trim().length() >= 8;
    }

    private boolean validateCredentials(ModifyCredentials details) {
        log.info(SERVICE_METHOD_ENTERED,VALIDATE_CREDENTIALS);
        if (details.getOldPassword().equals(details.getNewPassword())) {
            throw new UserException("Old and New password looks similar, they should be different", HttpStatus.BAD_REQUEST);
        }
        if (details.getUserName().trim().length() == 0 ) {
            throw new UserException("username is missing.", HttpStatus.BAD_REQUEST);
        }
        if(details.getOldPassword().trim().length() < 8 ||
                details.getNewPassword().trim().length() < 8) {
            throw new UserException("please mention old and new passwords at least 8 characters", HttpStatus.BAD_REQUEST);
        }
        log.info(SERVICE_METHOD_EXITED,VALIDATE_CREDENTIALS);
        return validatePassword(details.getNewPassword());

    }
    private boolean validatePassword(String newPassword) {
        log.info(SERVICE_METHOD_ENTERED,"validatePassword");
        return newPassword.matches(".*[!@#$%^&*].*") && newPassword.matches(".*\\d.*") && newPassword.matches(".*[A-Z].*") && newPassword.matches(".*[a-z].*") && newPassword.length() >= 8;
    }
}
