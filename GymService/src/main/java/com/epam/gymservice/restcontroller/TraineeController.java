package com.epam.gymservice.restcontroller;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TraineeRegistration;
import com.epam.gymservice.dto.request.TraineeUpdate;
import com.epam.gymservice.dto.request.TraineesTrainerUpdate;
import com.epam.gymservice.dto.response.TraineeDetails;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.kafka.Producer;
import com.epam.gymservice.service.AccountService;
import com.epam.gymservice.service.MailNotificationService;
import com.epam.gymservice.service.TraineeService;
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
@RequestMapping("/gym-service/trainee")
public class TraineeController {

    private static final String API_INVOKED = "{} Api entered in Trainee controller with request details {}";
    private static final String API_EXITED = "{} Api exited in Trainee controller.";

    private final TraineeService traineeServiceImpl;
    private final AccountService accountServiceImpl;
    private final MailNotificationService mailNotificationServiceImpl;
    private final Producer producer;

    @PostMapping("/trainee-signup")
    public ResponseEntity<CredentialsDetails> registerTraineeDetails(@RequestBody @Valid TraineeRegistration trainee) {
        log.info(API_INVOKED,"registerTraineeDetails",trainee);
        ResponseEntity<CredentialsDetails> response = new ResponseEntity<>(accountServiceImpl.saveTrainee(trainee), HttpStatus.OK);
        MailDTO mailDTO = mailNotificationServiceImpl.prepareMailForCredentials(response.getBody(),trainee.getMail());
        producer.sendUserToServer(mailDTO);
        log.info(API_EXITED,"registerTraineeDetails");
        return response;
    }

    @GetMapping("/fetch-details/{username}")
    public ResponseEntity<TraineeDetails> fetchTraineeInformation(@PathVariable @Valid String username) {
        log.info(API_INVOKED,"fetchTraineeInformation",username);
        ResponseEntity<TraineeDetails> response = new ResponseEntity<>(traineeServiceImpl.fetchTrainee(username),HttpStatus.OK);
        log.info(API_EXITED,"fetchTraineeInformation");
        return response;
    }

    @PutMapping("/update-trainee")
    public ResponseEntity<TraineeDetails> updateTraineeInformation(@RequestBody @Valid TraineeUpdate traineeUpdate) {
        log.info(API_INVOKED,"updateTraineeInformation",traineeUpdate);
        ResponseEntity<TraineeDetails> response = new ResponseEntity<>(traineeServiceImpl.updateTrainee(traineeUpdate),HttpStatus.OK);
        MailDTO mailDTO = mailNotificationServiceImpl.prepareMailForTrainee(traineeUpdate.getMail(),traineeUpdate);
        producer.sendUserToServer(mailDTO);
        log.info(API_EXITED,"updateTraineeInformation");
        return response;
    }

    @DeleteMapping("/delete-trainee/{username}")
    public ResponseEntity<HttpStatus> deleteTrainee(@PathVariable @Valid String username) {
        log.info(API_INVOKED,"deleteTrainee",username);
        traineeServiceImpl.deleteTrainee(username);
        log.info(API_EXITED,"deleteTrainee");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-trainees-trainers-list")
    public ResponseEntity<List<TrainerDetails>> updateTraineesTrainersInformation(@RequestBody @Valid TraineesTrainerUpdate traineesTrainerDetails) {
        log.info(API_INVOKED,"updateTraineesTrainersInformation",traineesTrainerDetails);
        ResponseEntity<List<TrainerDetails>> response = new ResponseEntity<>(traineeServiceImpl.updateTraineesTrainersInformation(traineesTrainerDetails),HttpStatus.OK);
        log.info(API_EXITED,"updateTraineesTrainersInformation");
        return response;
    }


}
