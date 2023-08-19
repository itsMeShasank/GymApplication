package com.epam.notificationservice.kafkaListener;

import com.epam.gymservice.dto.request.MailDTO;
import com.epam.notificationservice.listener.Consumer;
import com.epam.notificationservice.service.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ListenerTest {

    @Mock
    NotificationServiceImpl notificationService;

    @InjectMocks
    Consumer consumer;

    static MailDTO mailDTO;

    @BeforeAll
    static void setUp() {
        mailDTO = MailDTO.builder()
                .recipients(new ArrayList<>(List.of("shasank@gmail.com")))
                .message(new HashMap<>())
                .emailType("TRAINEE PROFILE UPDATED")
                .build();
    }

    @Test
    void getMailDtoFromServerTest() {
        Mockito.when(notificationService.sendNotification(any(MailDTO.class))).thenReturn("Mail Sent");
        consumer.getMailDtoFromServer(mailDTO);
    }
}
