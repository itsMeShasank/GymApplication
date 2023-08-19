package com.epam.gymservice.service;

import com.epam.gymservice.dto.request.TraineeTrainings;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TraineesTrainerUpdate;
import com.epam.gymservice.dto.response.TraineeDetails;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.dto.response.TrainingDetails;

import java.util.List;

public interface TraineeService {
    String SERVICE_METHOD_INVOKED = "method {} invoked in TraineeServiceImpl class.";
    String SERVICE_METHOD_EXITED = "method {} exited in TraineeServiceImpl class.";
    String FETCH_SERVICE_METHOD = "fetchService";
    TraineeDetails fetchTrainee(String userName) ;
    TraineeDetails updateTrainee(TraineeUpdate traineeDetails) ;
    void deleteTrainee(String username) ;
    List<TrainerDetails> updateTraineesTrainersInformation(TraineesTrainerUpdate traineesTrainerDetails) ;
    List<TrainingDetails> fetchTraineeTrainingInformation(TraineeTrainings traineeTrainings) ;
}
