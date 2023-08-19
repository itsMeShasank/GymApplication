package com.epam.gymservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingReport {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerIsActive;
    private LocalDate date;
    private Long duration;

}
