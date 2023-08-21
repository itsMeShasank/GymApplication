package com.epam.gymreportservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "training-reports")
public class Report {

    @Id
    private String trainerUsername;
    private String trainerFirstName;
    private String trainerLastName;
    private boolean trainerIsActive;
    Map<Long, Map<Long, Map<Long , Map<String ,Long>>>> durationSummary; //Year -> Month -> Day -> Date -> Duration


}
