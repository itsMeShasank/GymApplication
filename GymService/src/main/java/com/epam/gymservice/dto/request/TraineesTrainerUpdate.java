package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineesTrainerUpdate {

    @NotBlank(message = "Please provide Trainee Username")
    private String traineeUserName;
    @NotNull(message = "Please provide minimum one Trainer Username")
    private List<String> trainersUserName;
}
