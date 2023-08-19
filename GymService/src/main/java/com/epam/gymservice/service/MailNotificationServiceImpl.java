package com.epam.gymservice.service;

import com.epam.gymservice.dao.UserRepository;
import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.dto.request.Training;
import com.epam.gymservice.helper.UserNotFoundException;
import com.epam.gymservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class MailNotificationServiceImpl implements MailNotificationService{


    private final UserRepository userRepository;
    Map<String,String> message;

    public MailNotificationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        message = new LinkedHashMap<>();
    }
    @Override
    public MailDTO prepareMailForCredentials(CredentialsDetails credentialsDetails, String mail) {
        log.info(SERVICE_METHOD_ENTERED,"prepareMailForCredentials");

        message.put("username",credentialsDetails.getUserName());
        message.put("password",credentialsDetails.getPassword());
        MailDTO mailDTO = generateMail(new ArrayList<>(Collections.singletonList(mail)), "REGISTERED SUCCESSFUL", message);
        log.info(SERVICE_METHOD_EXITED,"prepareMailForCredentials");
        return mailDTO;
    }

    @Override
    public MailDTO prepareMailForTrainee(String mail, TraineeUpdate traineeDetails) {
        log.info(SERVICE_METHOD_ENTERED,"prepareMailForTrainee");
        message.put("traineeUsername",traineeDetails.getUserName());
        message.put("traineeFirstName",traineeDetails.getFirstName());
        message.put("traineeLastName",traineeDetails.getLastName());
        message.put("traineeDateOfBirth",traineeDetails.getDateOfBirth().toString());
        message.put("traineeAddress",traineeDetails.getAddress());

        MailDTO mailDTO = generateMail(new ArrayList<>(Collections.singletonList(mail)), "TRAINEE PROFILE UPDATED",message);
        log.info(SERVICE_METHOD_EXITED,"prepareMailForTrainee");
        return mailDTO;
    }

    @Override
    public MailDTO prepareMailForTrainer(String mail, TrainerUpdate trainerDetails) {
        log.info(SERVICE_METHOD_ENTERED,"prepareMailForTrainer");

        message.put("trainerUsername",trainerDetails.getUserName());
        message.put("trainerFirstName",trainerDetails.getFirstName());
        message.put("trainerLastName",trainerDetails.getLastName());
        message.put("trainerSpecialization",trainerDetails.getSpecialization());
        MailDTO mailDTO = generateMail(new ArrayList<>(Collections.singletonList(mail)),"TRAINER PROFILE UPDATED",message);
        log.info(SERVICE_METHOD_EXITED,"prepareMailForTrainer");
        return mailDTO;
    }

    @Override
    public MailDTO prepareMailForTraining(Training trainingDetails) {
        log.info(SERVICE_METHOD_ENTERED,"prepareMailForTraining");

        User trainer = userRepository.findByUserName(trainingDetails.getTrainerUserName())
                .orElseThrow(() -> new UserNotFoundException("No username found in database", HttpStatus.BAD_REQUEST));

        User trainee = userRepository.findByUserName(trainingDetails.getTraineeUserName())
                .orElseThrow(() -> new UserNotFoundException("No username found in database",HttpStatus.BAD_REQUEST));

        message.put("trainerFirstName",trainer.getFirstName());
        message.put("trainerLastName",trainer.getLastName());
        message.put("traineeFirstName",trainee.getFirstName());
        message.put("trainingName",trainingDetails.getTrainingName());
        message.put("trainingType",trainingDetails.getTrainingType());
        message.put("trainingDuration", String.valueOf(trainingDetails.getTrainingDuration()));
        message.put("trainingDate",trainingDetails.getTrainingDate().toString());

        MailDTO mailDTO = generateMail(new ArrayList<>(Arrays.asList(trainer.getMail(),trainee.getMail())), "NEW TRAINING REGISTERED",message);

        log.info(SERVICE_METHOD_EXITED,"prepareMailForTraining");
        return mailDTO;
    }

    private static MailDTO generateMail(List<String> mail,String subject,Map<String,String> message) {
        return MailDTO.builder()
                .recipients(mail)
                .emailType(subject)
                .message(message)
                .build();
    }
}
