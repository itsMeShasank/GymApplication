package com.epam.gymreportservice.listener;

import com.epam.gymreportservice.service.TrainingReportService;
import com.epam.gymservice.dto.request.TrainingReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class Consumer {

    private static final String CONSUMER_METHOD_INVOKED = "Method {} invoked in Consumer class.";
    private static final String CONSUMER_METHOD_EXITED = "Method {} exited in Consumer class.";
    private final TrainingReportService trainingReportServiceImpl;

    @KafkaListener(topics = "Trainings",groupId = "reports-group")
    public void getReportFromServer(TrainingReport trainingReport) {
        log.info(CONSUMER_METHOD_INVOKED,"getReportFromServer");
        trainingReportServiceImpl.insertTrainingReport(trainingReport);
        log.info(CONSUMER_METHOD_EXITED,"getReportFromServer");
    }
}
