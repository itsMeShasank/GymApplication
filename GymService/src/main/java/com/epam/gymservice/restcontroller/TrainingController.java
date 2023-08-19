package com.epam.gymservice.restcontroller;

import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TraineeTrainings;
import com.epam.gymservice.dto.request.TrainerTrainings;
import com.epam.gymservice.dto.request.TrainingReport;
import com.epam.gymservice.dto.response.TrainingDetails;
import com.epam.gymservice.kafka.Producer;
import com.epam.gymservice.service.MailNotificationService;
import com.epam.gymservice.service.TraineeService;
import com.epam.gymservice.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gym-service")
public class TrainingController {

    private static final String API_INVOKED = "{} Api entered in Training controller with request details {}";
    private static final String API_EXITED = "{} Api exited in Training controller.";
    private final TraineeService traineeServiceImpl;
    private final TrainerService trainerServiceImpl;
    private final MailNotificationService mailNotificationServiceImpl;
    private final Producer kafkaProducer;

    @PostMapping("/trainee-trainings-list")
    public ResponseEntity<List<TrainingDetails>> fetchTraineeTrainingInformation(@RequestBody @Valid TraineeTrainings traineeTrainingsDetails) {
        log.info(API_INVOKED,"fetchTraineeTrainingInformation",traineeTrainingsDetails);
        ResponseEntity<List<TrainingDetails>> response = new ResponseEntity<>(traineeServiceImpl.fetchTraineeTrainingInformation(traineeTrainingsDetails), HttpStatus.OK);
        log.info(API_EXITED,"fetchTraineeTrainingInformation");
        return response;
    }

    @PostMapping("/trainer-trainings-list")
    public ResponseEntity<List<TrainingDetails>> fetchTrainerTrainingsInformation(@RequestBody @Valid TrainerTrainings trainerTrainingsDetails) {
        log.info(API_INVOKED,"fetchTrainerTrainingsInformation",trainerTrainingsDetails);
        ResponseEntity<List<TrainingDetails>>response = new ResponseEntity<>(trainerServiceImpl.fetchTrainerTrainingsInformation(trainerTrainingsDetails),HttpStatus.OK);
        log.info(API_EXITED,"fetchTrainerTrainingsInformation");
        return response;
    }

    @PutMapping("/add-training")
    public ResponseEntity<HttpStatus> registerTrainingInformation(@RequestBody @Valid com.epam.gymservice.dto.request.Training trainingDetails) {
        log.info(API_INVOKED,"registerTrainingInformation",trainingDetails);
        TrainingReport trainingReport = trainerServiceImpl.registerTrainingInformation(trainingDetails);
        kafkaProducer.sendTrainingReportToServer(trainingReport);
        MailDTO mails = mailNotificationServiceImpl.prepareMailForTraining(trainingDetails);
        kafkaProducer.sendUserToServer(mails);
        log.info(API_EXITED,"registerTrainingInformation");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /*    @GetMapping("/training-report/{trainingName}")
    public ResponseEntity<TrainingReportResponse> fetchTrainingReport(@PathVariable @Valid String trainingName) {
        log.info(API_INVOKED,"fetchTrainingReport");
        ResponseEntity<TrainingReportResponse> trainingReport = gymReportProxy.fetchTrainingReport(trainingName);
        log.info(API_INVOKED,"fetchTrainingReport");
        return trainingReport;
    }*/


}
