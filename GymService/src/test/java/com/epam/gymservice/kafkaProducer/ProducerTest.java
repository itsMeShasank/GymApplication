package com.epam.gymservice.kafkaProducer;

import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TrainingReport;
import com.epam.gymservice.kafka.Producer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProducerTest {

    @Mock
    KafkaTemplate<String, MailDTO> mailDTOKafkaTemplate;
    @Mock
    KafkaTemplate<String, TrainingReport> trainingReportKafkaTemplate;
    @InjectMocks
    Producer producer;

    static MailDTO mailDTO;
    static TrainingReport trainingReport;
    @BeforeAll
    static void setUp() {
        mailDTO = MailDTO.builder()
                .emailType("PROFILE UPDATED.")
                .message(new HashMap<>())
                .recipients(new ArrayList<>(List.of("shasank@gmail.com")))
                .build();

        trainingReport = new TrainingReport();
        trainingReport.setTrainerUsername("shasank");
        trainingReport.setTrainerFirstName("shasank");
        trainingReport.setTrainerLastName("gadipilli");
        trainingReport.setDate(LocalDate.of(2023,8,20));
        trainingReport.setDuration(30L);
        trainingReport.setTrainerIsActive(true);
    }

    @Test
    void sendUserToServerTest() {
        Mockito.when(mailDTOKafkaTemplate.send(any(String.class),any(MailDTO.class))).thenReturn(null);
        producer.sendUserToServer(mailDTO);
    }

    @Test
    void sendTrainingReportToServerTest() {
        Mockito.when(trainingReportKafkaTemplate.send(any(String.class),any(TrainingReport.class))).thenReturn(null);
        producer.sendTrainingReportToServer(trainingReport);
    }
}
