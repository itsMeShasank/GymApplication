package com.epam.gymservice.restcontroller;

import com.epam.gymservice.service.TrainerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/gym-service")
public class TrainingTypeController {

    private static final String API_INVOKED = "{} Api entered in Training controller with request details {}";
    private static final String API_EXITED = "{} Api exited in Training controller.";

    private final TrainerService trainerServiceImpl;

    @PostMapping("/add-training-type")
    public ResponseEntity<HttpStatus> registerTrainingType(@RequestBody @Valid String specialization) {
        log.info(API_INVOKED,"registerTrainingType",specialization);
        trainerServiceImpl.saveTrainingType(specialization);
        log.info(API_EXITED,"registerTrainingType");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
