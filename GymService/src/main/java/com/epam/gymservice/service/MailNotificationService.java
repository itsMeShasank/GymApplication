package com.epam.gymservice.service;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.dto.request.Training;

public interface MailNotificationService {
    String SERVICE_METHOD_ENTERED = "method {} invoked in AccountServiceImpl class.";
    String SERVICE_METHOD_EXITED = "method {} exited in AccountServiceImpl class.";

    MailDTO prepareMailForCredentials(CredentialsDetails credentialsDetails, String mail);
    MailDTO prepareMailForTrainee(String mail, TraineeUpdate traineeDetails);
    MailDTO prepareMailForTrainer(String mail, TrainerUpdate trainerDetails);
    MailDTO prepareMailForTraining(Training trainingDetails);
}
