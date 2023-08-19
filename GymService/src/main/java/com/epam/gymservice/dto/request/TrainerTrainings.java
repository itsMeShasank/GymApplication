package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerTrainings {

    @NotBlank(message = "Please provide Username")
    private String trainerUserName;
    //@FutureOrPresent(message = "Please provide proper Start Date, either it should be Present Date or Future Date")
    private LocalDate startDate;
    //@FutureOrPresent(message = "Please provide proper end Date, either it should be Present Date or Future Date")
    private LocalDate endDate;
    //@NotBlank(message = "Please provide Trainee Name")
    private String traineeUserName;
}
