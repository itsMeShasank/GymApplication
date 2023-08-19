package com.epam.gymservice.restcontroller;

import com.epam.gymservice.dto.CredentialsDetails;
import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TrainerRegistration;
import com.epam.gymservice.dto.request.TrainerUpdate;
import com.epam.gymservice.dto.response.TrainerDetails;
import com.epam.gymservice.kafka.Producer;
import com.epam.gymservice.service.AccountService;
import com.epam.gymservice.service.MailNotificationService;
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
@RequestMapping("/gym-service/trainer")
public class TrainerController {
    private static final String API_INVOKED = "{} Api entered in Trainer controller with request details {}";
    private static final String API_EXITED = "{} Api exited in Trainer controller.";
    private final TrainerService trainerServiceImpl;
    private final AccountService accountServiceImpl;
    private final MailNotificationService mailNotificationServiceImpl;
    private final Producer producer;



    @PostMapping("/trainer-signup")
    public ResponseEntity<CredentialsDetails> registerTrainerDetails(@RequestBody @Valid TrainerRegistration trainer) {
        log.info(API_INVOKED,"registerTrainerDetails",trainer);
        ResponseEntity<CredentialsDetails>response = new ResponseEntity<>(accountServiceImpl.saveTrainer(trainer), HttpStatus.OK);
        MailDTO mailDTO = mailNotificationServiceImpl.prepareMailForCredentials(response.getBody(),trainer.getMail());
        producer.sendUserToServer(mailDTO);
        log.info(API_EXITED,"registerTrainerDetails");
        return response;
    }

    @GetMapping("/fetch-trainer/{username}")
    public ResponseEntity<TrainerDetails> fetchTrainerInformation(@PathVariable @Valid String username) {
        log.info(API_INVOKED,"fetchTrainerInformation",username);
        ResponseEntity<TrainerDetails> response = new ResponseEntity<>(trainerServiceImpl.fetchTrainer(username),HttpStatus.OK);
        log.info(API_EXITED,"fetchTrainerInformation");
        return response;
    }

    @PutMapping("/update-trainer")
    public ResponseEntity<TrainerDetails> updateTrainerInformation(@RequestBody @Valid TrainerUpdate trainer) {
        log.info(API_INVOKED,"updateTrainerInformation",trainer);
        ResponseEntity<TrainerDetails> response = new ResponseEntity<>(trainerServiceImpl.updateTrainer(trainer),HttpStatus.OK);
        MailDTO mailDTO = mailNotificationServiceImpl.prepareMailForTrainer(trainer.getMail(),trainer);
        producer.sendUserToServer(mailDTO);
        log.info(API_EXITED,"updateTrainerInformation");
        return response;
    }

    @GetMapping("/active-trainers/{traineeUsername}")
    public ResponseEntity<List<TrainerDetails>> fetchActiveTrainersNotAssignedOnTrainee(@PathVariable @Valid String traineeUsername) {
        log.info(API_INVOKED,"fetchActiveTrainers",traineeUsername);
        ResponseEntity<List<TrainerDetails>> response =  new ResponseEntity<>(trainerServiceImpl.fetchActiveTrainer(traineeUsername),HttpStatus.OK);
        log.info(API_EXITED,"fetchActiveTrainers");
        return response;
    }
}
