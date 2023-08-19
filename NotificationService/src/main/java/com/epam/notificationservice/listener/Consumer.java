package com.epam.notificationservice.listener;

import com.epam.gymservice.dto.request.MailDTO;
import com.epam.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Consumer {
    private static final String SERVICE_METHOD_INVOKED = "method {} invoked in Consumer class";
    private static final String SERVICE_METHOD_EXITED = "method {} exited in Consumer class";

    private final NotificationService notificationServiceImpl;

    @KafkaListener(topics = "notifications",groupId = "notification-group")
    public void getMailDtoFromServer(MailDTO mailDTO) {
        log.info(SERVICE_METHOD_INVOKED,"getMailDtoFromServer");
        notificationServiceImpl.sendNotification(mailDTO);
        log.info(SERVICE_METHOD_EXITED,"getMailDtoFromServer");
    }
}
