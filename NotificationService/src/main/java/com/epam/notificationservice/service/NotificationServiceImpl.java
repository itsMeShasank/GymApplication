package com.epam.notificationservice.service;

import com.epam.gymservice.dto.request.MailDTO;
import com.epam.notificationservice.dao.MailRepository;
import com.epam.notificationservice.model.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService{
    private final JavaMailSender javaMailSender;
    private final MailRepository mailRepository;

    @Value("${spring.mail.username}")
    private String fromMail;

    @Override
    public String sendNotification(MailDTO mailDTO) {
        log.info(SERVICE_METHOD_INVOKED,"sendNotification");
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(mailDTO.getRecipients().toArray(new String[0]));
        mailMessage.setFrom(fromMail);
        mailMessage.setSubject(mailDTO.getEmailType());
        String body = setBody(mailDTO.getMessage());
        mailMessage.setText(body);
        Mail mail = Mail.builder()
                .recipients(mailDTO.getRecipients())
                .subject(mailDTO.getEmailType())
                .body(body)
                .build();


        try {
            javaMailSender.send(mailMessage);
            mail.setStatus("SUCCESS");
            mail.setRemarks("Mail sent at"+ LocalDate.now());
        }catch (MailException exception) {
            mail.setStatus("FAILED");
            mail.setRemarks(exception.getMessage());
        }

        mailRepository.save(mail);
        log.info(SERVICE_METHOD_EXITED,"sendNotification");
        return "Mail sent..";

    }

    private String setBody(Map<String,String> bodyParameters) {
        return bodyParameters.entrySet().stream().map(entry -> entry.getKey() + " : " + entry.getValue()).collect(Collectors.joining("\n"));
    }


}
