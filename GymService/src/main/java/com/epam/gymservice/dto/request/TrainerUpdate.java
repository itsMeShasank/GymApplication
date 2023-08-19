package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerUpdate {

    @NotBlank(message = "Please provide Username")
    private String userName;
    @NotBlank(message = "Please provide First name")
    private String firstName;
    @NotBlank(message = "Please provide Last name")
    private String lastName;
    @Email(message = "Please provide proper email")
    private String mail;
    @NotBlank(message = "Please provide Specialization")
    private String specialization;
    @NotNull(message = "Please provide whether you're active or not.")
    private boolean isActive;
}
