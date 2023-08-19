package com.epam.notificationservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@AllArgsConstructor
public class ExceptionResponse {

    private String timestamp;
    private String status;
    private String error;
    private String path;

}
