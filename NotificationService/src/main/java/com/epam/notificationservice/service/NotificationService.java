package com.epam.notificationservice.service;

import com.epam.gymservice.dto.request.MailDTO;
import org.springframework.mail.MailException;

public interface NotificationService {
    String SERVICE_METHOD_INVOKED = "method {} invoked in NotificationServiceImpl class";
    String SERVICE_METHOD_EXITED = "method {} exited in NotificationServiceImpl class";

    String sendNotification(MailDTO mailDTO) throws MailException;
}
