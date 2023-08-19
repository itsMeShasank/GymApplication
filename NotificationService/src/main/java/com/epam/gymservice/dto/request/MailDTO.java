package com.epam.gymservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MailDTO {

    private List<String> recipients;
    private Map<String,String> message;
    private String emailType;
}
