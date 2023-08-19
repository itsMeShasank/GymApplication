package com.epam.gymservice.service;

import com.epam.gymservice.dao.TraineeRepository;
import com.epam.gymservice.dao.TrainingTypeRepository;
import com.epam.gymservice.dao.UserRepository;
import com.epam.gymservice.dto.request.TraineeTrainings;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TraineesTrainerUpdate;
import com.epam.gymservice.dto.response.TraineeDetails;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.dto.response.TrainingDetails;
import com.epam.gymservice.helper.TraineeNotFoundException;
import com.epam.gymservice.helper.TrainerNotFoundException;
import com.epam.gymservice.helper.UserException;
import com.epam.gymservice.helper.UserNotFoundException;
import com.epam.gymservice.model.Trainee;
import com.epam.gymservice.model.Trainer;
import com.epam.gymservice.model.TrainingType;
import com.epam.gymservice.model.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TraineeServiceImpl implements TraineeService {

    private static final String SET_TRAINEE_DETAILS = "setTraineeDetails";
    private final UserRepository userRepository;
    private final TraineeRepository traineeRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final AccountService accountServiceImpl;

    @Override
    public TraineeDetails fetchTrainee(String userName) {
        log.info(SERVICE_METHOD_INVOKED,FETCH_SERVICE_METHOD);
        if (userName.trim().length() == 0) {
            throw new UserException("Please provide username", HttpStatus.BAD_REQUEST);
        }
        User userFound = accountServiceImpl.getUserFromDatabase(userName);
        Trainee trainee = Optional.ofNullable(userFound.getTrainee())
                .orElseThrow(() -> new TraineeNotFoundException("No Trainee found with provided username", HttpStatus.BAD_REQUEST));
        TraineeDetails traineeDetails =  setTraineeDetails(userFound, trainee);
        log.info(SERVICE_METHOD_EXITED,FETCH_SERVICE_METHOD);
        return traineeDetails;
    }

    private TraineeDetails setTraineeDetails(User userFound, Trainee trainee) {
        log.info(SERVICE_METHOD_INVOKED,SET_TRAINEE_DETAILS);
        TraineeDetails traineeDetails = new TraineeDetails();
        traineeDetails.setFirstName(userFound.getFirstName());
        traineeDetails.setLastName(userFound.getLastName());
        traineeDetails.setMail(userFound.getMail());
        traineeDetails.setActive(userFound.isActive());
        traineeDetails.setAddress(trainee.getAddress());
        traineeDetails.setDateOfBirth(trainee.getDateOfBirth());
        List<TrainerDetails> trainers = getTrainerDetailsList(trainee);
        traineeDetails.setTrainersList(trainers);
        log.info(SERVICE_METHOD_EXITED,SET_TRAINEE_DETAILS);
        return traineeDetails;
    }

    private List<TrainerDetails> getTrainerDetailsList(Trainee trainee) {
        log.info(SERVICE_METHOD_INVOKED,"getTrainerDetailsList");
        List<TrainerDetails> trainerDetailsList =  trainee.getTrainers()
                .stream()
                .map(Trainer::getUser)
                .map(user -> userRepository.findById(user.getId()).get())
                .map(userDetails -> {
                    TrainerDetails trainerDetails = new TrainerDetails();
                    trainerDetails.setFirstName(userDetails.getFirstName());
                    trainerDetails.setLastName(userDetails.getLastName());
                    trainerDetails.setSpecialization(userDetails.getTrainer()
                            .getSpecialization()
                            .getSpecialization());
                    return trainerDetails;
                })
                .toList();
        log.info(SERVICE_METHOD_EXITED,"getTrainerDetailsList");
        return trainerDetailsList;
    }

    @Override
    public TraineeDetails updateTrainee(TraineeUpdate traineeUpdatedDetails) {
        log.info(SERVICE_METHOD_INVOKED,"updateTrainee");
        User userFound = accountServiceImpl.getUserFromDatabase(traineeUpdatedDetails.getUserName());
        Trainee trainee = Optional.ofNullable(userFound.getTrainee())
                .orElseThrow(()-> new TraineeNotFoundException("No Trainee found with username", HttpStatus.BAD_REQUEST));
        log.info(SERVICE_METHOD_EXITED,"updateTrainee");
        return setTraineeDetails(userFound, trainee, traineeUpdatedDetails);
    }

    private TraineeDetails setTraineeDetails(User userFound, Trainee traineeFound, TraineeUpdate traineeUpdatedDetails) {
        log.info(SERVICE_METHOD_INVOKED,SET_TRAINEE_DETAILS);
        userFound.setUserName(traineeUpdatedDetails.getUserName());
        userFound.setFirstName(traineeUpdatedDetails.getFirstName());
        userFound.setLastName(traineeUpdatedDetails.getLastName());
        userFound.setMail(traineeUpdatedDetails.getMail());
        userFound.setActive(traineeUpdatedDetails.isActive());
        traineeFound.setDateOfBirth(traineeUpdatedDetails.getDateOfBirth());
        traineeFound.setAddress(traineeUpdatedDetails.getAddress());

        userRepository.save(userFound);
        traineeRepository.save(traineeFound);
        TraineeDetails traineeDetails =  mapUserToTraineeDetailsResponse(userFound,traineeFound);
        log.info(SERVICE_METHOD_EXITED,SET_TRAINEE_DETAILS);
        return traineeDetails;
    }

    private TraineeDetails mapUserToTraineeDetailsResponse(User userFound, Trainee traineeFound) {
        log.info(SERVICE_METHOD_INVOKED,"mapUserToTraineeDetailsResponse");
        TraineeDetails traineeDetails = new TraineeDetails();
        traineeDetails.setUserName(userFound.getUserName());
        traineeDetails.setFirstName(userFound.getFirstName());
        traineeDetails.setLastName(userFound.getLastName());
        traineeDetails.setMail(userFound.getMail());
        traineeDetails.setDateOfBirth(traineeFound.getDateOfBirth());
        traineeDetails.setAddress(traineeFound.getAddress());
        traineeDetails.setActive(userFound.isActive());
        traineeDetails.setTrainersList(mapTraineeTrainersDetails(traineeFound));
        log.info(SERVICE_METHOD_EXITED,"mapUserToTraineeDetailsResponse");
        return traineeDetails;
    }

    public List<TrainerDetails> mapTraineeTrainersDetails(Trainee traineeFound) {
        log.info(SERVICE_METHOD_INVOKED,"mapTraineeTrainersDetails");
        List<TrainerDetails> trainerDetailsList =  traineeFound.getTrainers().stream()
                .map(trainer -> {
                    TrainerDetails trainerDetails = new TrainerDetails();
                    trainerDetails.setUserName(trainer.getUser().getUserName());
                    trainerDetails.setFirstName(trainer.getUser().getFirstName());
                    trainerDetails.setLastName(trainer.getUser().getLastName());
                    trainerDetails.setSpecialization(trainingTypeRepository.findById(trainer.getSpecialization()
                                    .getId())
                            .get()
                            .getSpecialization());
                    return trainerDetails;
                }).toList();
        log.info(SERVICE_METHOD_EXITED,"mapTraineeTrainersDetails");
        return trainerDetailsList;
    }
    @Override
    public void deleteTrainee(String username) {
        log.info(SERVICE_METHOD_INVOKED,"deleteTrainee");
        User userFound = accountServiceImpl.getUserFromDatabase(username);
        Trainee traineeFound = Optional.ofNullable(userFound.getTrainee())
                .orElseThrow(() -> new TraineeNotFoundException("No Trainee found with given username",HttpStatus.BAD_REQUEST));
        traineeFound.getTrainers().forEach(trainer -> trainer.getTrainees().remove(traineeFound));
        userRepository.delete(userFound);
        log.info(SERVICE_METHOD_EXITED,"deleteTrainee");
    }
    @Transactional
    @Override
    public List<TrainerDetails> updateTraineesTrainersInformation(TraineesTrainerUpdate traineesTrainerUpdatedDetails) {
        log.info(SERVICE_METHOD_INVOKED,"updateTraineesTrainersInformation");
        User userFound = accountServiceImpl.getUserFromDatabase(traineesTrainerUpdatedDetails.getTraineeUserName());

        Trainee traineeFound = Optional.ofNullable(userFound.getTrainee())
                .orElseThrow(() -> new UserNotFoundException("No user found with provided username as Trainee",HttpStatus.BAD_REQUEST));

        List<Trainer> existingTrainers = traineeFound.getTrainers();

        List<User> usersList = traineesTrainerUpdatedDetails.getTrainersUserName().stream()
                .map(accountServiceImpl::getUserFromDatabase)
                .toList();

        List<Trainer> newTrainers = usersList.stream()
                .map(user -> Optional.ofNullable(user.getTrainer())
                        .orElseThrow(()->new TrainerNotFoundException("No Trainer found with given username: "+user.getUserName(),HttpStatus.BAD_REQUEST)))
                .toList();

        existingTrainers.clear();
        existingTrainers.addAll(newTrainers);
        userFound.getTrainee().setTrainers(new ArrayList<>(new HashSet<>(existingTrainers)));
        log.info(SERVICE_METHOD_EXITED,"updateTraineesTrainersInformation");

        return userFound.getTrainee().getTrainers().stream()
                .map(trainer -> {
                    TrainerDetails trainerInfo = new TrainerDetails();
                    trainerInfo.setUserName(trainer.getUser().getUserName());
                    trainerInfo.setFirstName(trainer.getUser().getFirstName());
                    trainerInfo.setLastName(trainer.getUser().getLastName());
                    trainerInfo.setSpecialization(trainer.getSpecialization().getSpecialization());
                    return trainerInfo;
                }).toList();
    }

    @Override
    public List<TrainingDetails> fetchTraineeTrainingInformation(TraineeTrainings traineeTrainings) {
        log.info(SERVICE_METHOD_INVOKED,"fetchTraineeTrainingInformation");
        User userFound = accountServiceImpl.getUserFromDatabase(traineeTrainings.getTraineeUserName());
        TrainingType trainingType = trainingTypeRepository.findBySpecialization(traineeTrainings.getTrainingType()).orElse(null);
        List<com.epam.gymservice.model.Training> trainingsList = traineeRepository.findAllTrainingInBetween(traineeTrainings.getStartDate(),traineeTrainings.getEndDate(),userFound.getTrainee(),trainingType);
        log.info(SERVICE_METHOD_EXITED,"fetchTraineeTrainingInformation");
        return setTrainingDetails(trainingsList,userFound);

    }
    private List<TrainingDetails> setTrainingDetails(List<com.epam.gymservice.model.Training> trainingList, User userFound) {
        log.info(SERVICE_METHOD_INVOKED,"setTrainingDetails");
        List<TrainingDetails> trainingsList =  trainingList.stream()
                .map(training -> {
                    TrainingDetails trainingDetails = new TrainingDetails();
                    trainingDetails.setTrainingName(training.getTrainingName());
                    trainingDetails.setTrainingDate(training.getTrainingDate());
                    trainingDetails.setTrainingType(training.getTrainingType().getSpecialization());
                    trainingDetails.setTrainingDuration(training.getTrainingDuration());
                    trainingDetails.setTrainerName(training.getTrainer().getUser().getUserName());
                    trainingDetails.setTraineeName(userFound.getUserName());
                    return trainingDetails;
                }).toList();
        log.info(SERVICE_METHOD_EXITED,"setTrainingDetails");
        return trainingsList;
    }

}