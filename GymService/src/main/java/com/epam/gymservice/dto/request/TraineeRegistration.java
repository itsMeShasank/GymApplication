package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeRegistration {

    @NotBlank(message = "Please provide first name")
    private String firstName;

    @NotBlank(message = "Please provide last name")
    private String lastName;

    @Email(message = "Please provide proper mail address")
    private String mail;

    @Past(message = "The date must be in the future and format should be, dd-MM-yyyy")
    private LocalDate dateOfBirth;

    @Size(max=150,message = "Address must be between 0 and 100 characters")
    private String address;
}
