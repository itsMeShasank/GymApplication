package com.epam.gymservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TrainerDetails {

    private String userName;
    private String firstName;
    private String lastName;
    private String mail;
    private String specialization;
    private boolean isActive;
    private List<TraineeDetails> traineeDetailsList;
}
