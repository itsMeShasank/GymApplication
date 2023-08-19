package com.epam.authenticationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CredentialsDetails {

    @NotBlank(message = "Please provide username")
    private String userName;
    @NotBlank(message = "Please provide username")
    private String password;
}
