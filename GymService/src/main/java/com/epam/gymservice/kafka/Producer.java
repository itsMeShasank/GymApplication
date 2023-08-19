package com.epam.gymservice.kafka;

import com.epam.gymservice.dto.request.MailDTO;
import com.epam.gymservice.dto.request.TrainingReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class Producer {

    private final KafkaTemplate<String, MailDTO> mailKafkaTemplate;
    private final KafkaTemplate<String, TrainingReport> trainingReportKafkaTemplate;
    public void sendUserToServer(MailDTO mailDTO) {
        mailKafkaTemplate.send("notifications",mailDTO);
    }
    public void sendTrainingReportToServer(TrainingReport trainingReport) {
        trainingReportKafkaTemplate.send("Trainings",trainingReport);
    }

}
