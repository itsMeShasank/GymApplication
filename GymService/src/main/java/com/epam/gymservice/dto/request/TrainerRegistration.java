package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerRegistration {

    @NotBlank(message = "Please provide first name")
    private String firstName;
    @NotBlank(message = "Please provide last name")
    private String lastName;
    @Email(message = "Please provide proper email address")
    private String mail;
    @NotBlank(message = "Please provide specialization")
    private String specialization;
}
