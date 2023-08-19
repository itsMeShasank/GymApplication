package com.epam.gymservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class TraineeDetails {

    private String userName;
    private String firstName;
    private String lastName;
    private String mail;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TrainerDetails> trainersList;
    
}
