package com.epam.gymreportservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingReportResponse {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerIsActive;
    Map<Long, Map<Long, Map<Long , Map<String ,Long>>>> durationSummary; //Year -> Month -> Day -> Date -> Duration
}
