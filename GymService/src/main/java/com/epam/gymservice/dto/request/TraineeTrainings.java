package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainings {

    @NotBlank(message = "Please provide proper Trainee Name" )
    private String traineeUserName;
    private LocalDate startDate;
    private LocalDate endDate;
    private String trainerName;
    private String trainingType;
}
