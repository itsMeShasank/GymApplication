package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Training {

    @NotBlank(message = "Please provide Trainee Username")
    private String traineeUserName;
    @NotBlank(message = "Please provide Trainer Username")
    private String trainerUserName;
    @NotBlank(message = "Please provide Training Name")
    private String trainingName;
    @FutureOrPresent(message = "please provide Proper Training Date,either it can be Present Date or Future Date.")
    private LocalDate trainingDate;
    @NotBlank(message = "Please provide Proper Training Type")
    private String trainingType;
    @NotNull(message = "Please provide proper Training Duration")
    private Long trainingDuration;

}
