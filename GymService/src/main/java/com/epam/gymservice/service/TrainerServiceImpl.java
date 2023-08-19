package com.epam.gymservice.service;

import com.epam.gymservice.dao.TrainerRepository;
import com.epam.gymservice.dao.TrainingRepository;
import com.epam.gymservice.dao.TrainingTypeRepository;
import com.epam.gymservice.dao.UserRepository;
import com.epam.gymservice.dto.request.TrainerTrainings;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.dto.request.TrainingReport;
import com.epam.gymservice.dto.response.TraineeDetails;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.dto.response.TrainingDetails;
import com.epam.gymservice.helper.*;
import com.epam.gymservice.model.Trainee;
import com.epam.gymservice.model.Trainer;
import com.epam.gymservice.model.TrainingType;
import com.epam.gymservice.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainerServiceImpl implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final UserRepository userRepository;
    private final TrainingTypeRepository trainingTypeRepository;
    private final TrainingRepository trainingRepository;
    private final AccountService accountServiceImpl;


    @Override
    public TrainerDetails fetchTrainer(String username) {
        log.info(SERVICE_METHOD_INVOKED,"fetchTrainer");
        if (username.trim().length() == 0) {
            throw new UserException("Please provide username", HttpStatus.BAD_REQUEST);
        }
        User userFound = accountServiceImpl.getUserFromDatabase(username);
        Trainer trainer = userFound.getTrainer();
        if (trainer == null)
            throw new TrainerNotFoundException("Given username not found as Trainer", HttpStatus.BAD_REQUEST);
        TrainerDetails trainerDetails =  mapUserToTrainerDetails(userFound);
        log.info(SERVICE_METHOD_EXITED,"fetchTrainer");
        return trainerDetails;
    }


    private TrainerDetails mapUserToTrainerDetails(User userFound) {
        log.info(SERVICE_METHOD_INVOKED,MAP_USER_TO_TRAINER_DETAILS);
        TrainerDetails trainerDetails = new TrainerDetails();
        trainerDetails.setFirstName(userFound.getFirstName());
        trainerDetails.setLastName(userFound.getLastName());
        trainerDetails.setSpecialization(userFound.getTrainer().getSpecialization().getSpecialization());
        trainerDetails.setActive(userFound.isActive());
        log.info(SERVICE_METHOD_EXITED,MAP_USER_TO_TRAINER_DETAILS);
        return setTrainerTraineeDetails(trainerDetails,userFound.getTrainer());
    }
    @Override
    public TrainerDetails updateTrainer(TrainerUpdate trainerUpdatedDetails) {
        log.info(SERVICE_METHOD_INVOKED,"updateTrainer");
        if (!validateTrainer(trainerUpdatedDetails)) {
            throw new UserException("You're missing few fields", HttpStatus.BAD_REQUEST);
        }
        log.info(SERVICE_METHOD_EXITED,"validateTrainer");
        User userFound = accountServiceImpl.getUserFromDatabase(trainerUpdatedDetails.getUserName());
        return mapUserToTrainerDetails(userFound, trainerUpdatedDetails);
    }
    private boolean validateTrainer(TrainerUpdate trainerDetails) {
        log.info(SERVICE_METHOD_INVOKED,"validateTrainer");
        return trainerDetails.getFirstName().trim().length() != 0 &&
                trainerDetails.getLastName().trim().length() != 0 &&
                trainerDetails.getUserName().trim().length() != 0 &&
                trainerDetails.getSpecialization().trim().length() != 0;
    }
    private TrainerDetails mapUserToTrainerDetails(User userFound, TrainerUpdate trainerUpdatedDetails) {
        log.info(SERVICE_METHOD_INVOKED,"mapUserToTrainerDetails");
        //user details setting and saving to database.
        userFound.setUserName(trainerUpdatedDetails.getUserName());
        userFound.setFirstName(trainerUpdatedDetails.getFirstName());
        userFound.setLastName(trainerUpdatedDetails.getLastName());
        userFound.setMail(trainerUpdatedDetails.getMail());
        userFound.setActive(trainerUpdatedDetails.isActive());

        //fetching Trainer details
        Trainer trainer = Optional.ofNullable(userFound.getTrainer())
                .orElseThrow(() -> new TrainerNotFoundException("No Trainer Found with given username", HttpStatus.BAD_REQUEST));
        /*//yoga
        Optional<String> oldTrainingType = Optional.of(trainer.getSpecialization().getSpecialization());

        //zumba
        //fetching Training mapped to Trainer
        TrainingType trainerTrainingType = trainingTypeRepository.findBySpecialization(trainerUpdatedDetails.getSpecialization())
                .orElseThrow(() -> new GymApplicationException("No Training type is saved in database.",HttpStatus.BAD_REQUEST));

        //deleting old mapped training type and mapping to new training
        if(!oldTrainingType.get().equals(trainerTrainingType.getSpecialization())){
            oldTrainingType.ifPresent(trainingTypeRepository::deleteBySpecialization);
        }
        trainer.setSpecialization(trainerTrainingType);*/

        userRepository.save(userFound);
        //trainingTypeRepository.save(trainerTrainingType);
        trainerRepository.save(trainer);

        TrainerDetails trainerDetails = new TrainerDetails();
        trainerDetails.setUserName(userFound.getUserName());
        trainerDetails.setFirstName(userFound.getFirstName());
        trainerDetails.setLastName(userFound.getLastName());
        trainerDetails.setSpecialization(trainerUpdatedDetails.getSpecialization());
        log.info(SERVICE_METHOD_EXITED,"mapUserToTrainerDetails");
        return setTrainerTraineeDetails(trainerDetails,trainer);
    }
    private TrainerDetails setTrainerTraineeDetails(TrainerDetails trainerDetails,Trainer trainer) {
        log.info(SERVICE_METHOD_INVOKED,"setTraineeDetails");
        trainerDetails.setTraineeDetailsList(trainer.getTrainees().stream()
                .map(trainee -> {
                    TraineeDetails traineeDetails = new TraineeDetails();
                    traineeDetails.setUserName(trainee.getUser().getUserName());
                    traineeDetails.setFirstName(trainee.getUser().getFirstName());
                    traineeDetails.setLastName(trainee.getUser().getLastName());
                    traineeDetails.setActive(trainee.getUser().isActive());
                    return traineeDetails;
                }).toList());
        log.info(SERVICE_METHOD_EXITED,"setTraineeDetails");
        return trainerDetails;
    }

    @Override
    public List<TrainerDetails> fetchActiveTrainer(String username) {
        log.info(SERVICE_METHOD_INVOKED,"fetchActiveTrainer");
        User userFound = accountServiceImpl.getUserFromDatabase(username);
        Trainee traineeFound = Optional.ofNullable(userFound.getTrainee())
                .orElseThrow(() -> new TraineeNotFoundException("No Trainee found with provided username", HttpStatus.BAD_REQUEST));

        List<Trainer> activeTrainers = trainerRepository.findTrainersNotInList(traineeFound.getTrainers());
        
        log.info(SERVICE_METHOD_EXITED,"fetchActiveTrainer");
        return activeTrainers.stream()
                .map(trainer -> {
                    TrainerDetails trainerDetails = new TrainerDetails();
                    trainerDetails.setUserName(trainer.getUser().getUserName());
                    trainerDetails.setFirstName(trainer.getUser().getFirstName());
                    trainerDetails.setLastName(trainer.getUser().getLastName());
                    trainerDetails.setLastName(trainer.getSpecialization().getSpecialization());
                    return trainerDetails;
                }).toList();
    }
    @Override
    public List<TrainingDetails> fetchTrainerTrainingsInformation(TrainerTrainings trainerTrainingsDetails) {
        log.info(SERVICE_METHOD_INVOKED,"fetchTrainerTrainingsInformation");
        User userFound = accountServiceImpl.getUserFromDatabase(trainerTrainingsDetails.getTrainerUserName());
        Trainer trainer = Optional.ofNullable(userFound.getTrainer())
                .orElseThrow(() ->new TrainerNotFoundException("No Trainer found with given trainer username",HttpStatus.BAD_REQUEST));

        Optional<String> traineeName = Optional.ofNullable(trainerTrainingsDetails.getTraineeUserName());
        Trainee traineeFound = userFound.getTrainee();
        if(traineeName.isPresent()) {
            User userTrainee = accountServiceImpl.getUserFromDatabase(traineeName.get());
            traineeFound = userTrainee.getTrainee();
        }
        
        List<com.epam.gymservice.model.Training> trainings = trainerRepository.findAllTrainingInBetween(trainerTrainingsDetails.getStartDate(),trainerTrainingsDetails.getEndDate(),trainer,traineeFound);
        log.info(SERVICE_METHOD_EXITED,"fetchTrainerTrainingsInformation");
        return trainings.stream()
                .map(training1 -> {
                    TrainingDetails trainingDetails = new TrainingDetails();
                    trainingDetails.setTrainingName(training1.getTrainingName());
                    trainingDetails.setTrainingDate(training1.getTrainingDate());
                    trainingDetails.setTrainingType(training1.getTrainingType().getSpecialization());
                    trainingDetails.setTrainingDuration(training1.getTrainingDuration());
                    trainingDetails.setTraineeName(training1.getTrainee().getUser().getUserName());
                    return trainingDetails;
                }).toList();
    }
    @Override
    public TrainingReport registerTrainingInformation(com.epam.gymservice.dto.request.Training trainingDetails) {
        log.info(SERVICE_METHOD_INVOKED,"registerTrainingInformation");
        User trainee = accountServiceImpl.getUserFromDatabase(trainingDetails.getTraineeUserName());
        User trainer = accountServiceImpl.getUserFromDatabase(trainingDetails.getTrainerUserName());
        TrainingType trainingType = trainingTypeRepository.findBySpecialization(trainingDetails.getTrainingType())
                .orElseThrow(() -> new TrainingNotFoundException("No Training found with given details",HttpStatus.BAD_REQUEST));

        String trainerSpecialization = trainer.getTrainer().getSpecialization().getSpecialization();
        if(isTrainerNotBelongToTraining(trainerSpecialization,trainingDetails.getTrainingType())) {
            throw new AssociationException("Trainer doesn't belongs to provided training type.",HttpStatus.BAD_REQUEST);
        }

        com.epam.gymservice.model.Training training = new com.epam.gymservice.model.Training();
        training.setTrainingName(trainingDetails.getTrainingName());
        training.setTrainingDate(trainingDetails.getTrainingDate());
        training.setTrainingDuration(trainingDetails.getTrainingDuration());

        training.setTrainer(trainer.getTrainer());
        training.setTrainee(trainee.getTrainee());
        training.setTrainingType(trainingType);
        

        TrainingReport trainingReport = TrainingReport.builder()
                        .trainerUsername(trainer.getUserName())
                                .trainerFirstName(trainer.getFirstName())
                                .trainerLastName(trainer.getLastName())
                                .duration(trainingDetails.getTrainingDuration())
                                .date(trainingDetails.getTrainingDate())
                                .build();

        trainingRepository.save(training);
        log.info(SERVICE_METHOD_EXITED,"registerTrainingInformation");
        return trainingReport;
    }

    private boolean isTrainerNotBelongToTraining(String trainerSpecialization,String newSpecialization) {
        return !trainerSpecialization.equals(newSpecialization);
    }

    @Override
    public void saveTrainingType(String specialization) {
        log.info(SERVICE_METHOD_INVOKED,"saveTrainingType");
        TrainingType trainingTypeDetails = new TrainingType();
        trainingTypeDetails.setSpecialization(specialization);
        trainingTypeRepository.save(trainingTypeDetails);
        log.info(SERVICE_METHOD_EXITED,"saveTrainingType");
    }

}
