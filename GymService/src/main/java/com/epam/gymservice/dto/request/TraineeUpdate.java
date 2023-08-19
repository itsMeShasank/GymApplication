package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeUpdate {

    @NotBlank(message = "Please provide Username")
    private String userName;
    @NotBlank(message = "Please provide Username")
    private String firstName;
    @NotBlank(message = "Please provide Username")
    private String lastName;
    @Email(message = "Please provide proper mail")
    private String mail;
    @Past(message = "Please provide proper Date of Birth")
    private LocalDate dateOfBirth;
    @NotBlank(message = "Please provide Username")
    private String address;
    @NotNull(message = "Please provide whether you're active or not.")
    private boolean isActive;
}
