package com.epam.gymservice.service;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.ModifyCredentials;
import com.epam.gymservice.dto.request.TraineeRegistration;
import com.epam.gymservice.dto.request.TrainerRegistration;
import com.epam.gymservice.model.User;

public interface AccountService {
    String SERVICE_METHOD_ENTERED = "method {} invoked in AccountServiceImpl class.";
    String SERVICE_METHOD_EXITED = "method {} exited in AccountServiceImpl class.";
    String VALIDATE_CREDENTIALS = "validateCredentials";

    boolean validateUser(CredentialsDetails details);

    User getUserFromDatabase(String userName);

    boolean updateUser(ModifyCredentials details);

    CredentialsDetails saveTrainee(TraineeRegistration trainee);

    CredentialsDetails saveTrainer(TrainerRegistration trainerDetails);

}
