package com.epam.gymservice.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class TrainingReportResponse {

    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerIsActive;
    Map<Long, Map<Long, Map<Long , Map<String ,Long>>>> durationSummary;
}
