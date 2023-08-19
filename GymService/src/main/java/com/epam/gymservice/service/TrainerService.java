package com.epam.gymservice.service;

import com.epam.gymservice.dto.request.TrainerTrainings;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.dto.request.TrainingReport;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.dto.response.TrainingDetails;

import java.util.List;

public interface TrainerService {
    String SERVICE_METHOD_INVOKED = "method {} invoked in TrainerServiceImpl class.";
    String SERVICE_METHOD_EXITED = "method {} exited in TrainerServiceImpl class.";
    String MAP_USER_TO_TRAINER_DETAILS = "mapUserToTrainerDetails";

    TrainerDetails fetchTrainer(String username);
    TrainerDetails updateTrainer(TrainerUpdate trainerDetails);
    List<TrainerDetails> fetchActiveTrainer(String username);
    List<TrainingDetails> fetchTrainerTrainingsInformation(TrainerTrainings trainerTrainingsDetails);
    TrainingReport registerTrainingInformation(com.epam.gymservice.dto.request.Training trainingDetails);
    void saveTrainingType(String specialization);
}
