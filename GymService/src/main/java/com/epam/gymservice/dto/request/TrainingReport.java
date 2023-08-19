package com.epam.gymservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrainingReport {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerIsActive;
    private LocalDate date;
    private Long duration;
    //Map<Long, Map<Long, Map<Long , Map<String ,Long>>>> TrainingSummary; //Year -> Month -> Day -> Date -> Duration


}
