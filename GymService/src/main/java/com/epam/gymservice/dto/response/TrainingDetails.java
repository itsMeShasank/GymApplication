package com.epam.gymservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainingDetails {

    private String trainingName;
    private LocalDate trainingDate;
    private String trainingType;
    private Long trainingDuration;
    private String trainerName;
    private String traineeName;
}
