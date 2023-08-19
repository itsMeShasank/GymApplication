package com.epam.gymservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyCredentials {

    @NotBlank(message = "Please provide Username")
    private String userName;
    @Size(min = 8,message = "Please provide minimum 8 characters of Old Password")
    private String oldPassword;
    @Size(min = 8,message = "Please provide minimum 8 characters of New Password")
    private String newPassword;
}
