package com.epam.notificationservice.service;


import com.epam.gymservice.dto.request.MailDTO;
import com.epam.notificationservice.dao.MailRepository;
import com.epam.notificationservice.model.Mail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class NotificationServiceTest {

    @Mock
    JavaMailSender javaMailSender;
    @Mock
    MailRepository mailRepository;
    @InjectMocks
    NotificationServiceImpl notificationService;
    Mail mail;
    MailDTO mailDTO;

    @Mock
    SimpleMailMessage simpleMailMessage;

    @BeforeEach
    void setUp() {
        Map<String,String> messageBody = new HashMap<>();
        messageBody.put("body","This is for testing");
        mailDTO = MailDTO.builder()
                .recipients(new ArrayList<>(List.of("shasank@gmail.com")))
                .message(messageBody)
                .emailType("PROFILE UPDATED.")
                .build();

        mail = Mail.builder()
                .subject("profile update")
                .body("this is for testing")
                .recipients(new ArrayList<>(List.of("shasank@gmail.com")))
                .remarks("SENT")
                .status("SUCCESS").build();
    }

    @Test
    void sendNotificationTest() {
        Mockito.doNothing().when(javaMailSender).send(simpleMailMessage);
        Mockito.when(mailRepository.save(mail)).thenReturn(mail);
        assertEquals("Mail sent..",notificationService.sendNotification(mailDTO));
    }

    @Test
    void sendNotificationExceptionTest() {
        Mockito.doThrow(new MailException("Mail sending failed") {}).when(javaMailSender).send(any(SimpleMailMessage.class));
        notificationService.sendNotification(mailDTO);
    }


}
