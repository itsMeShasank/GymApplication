package com.epam.gymreportservice.listener;

import com.epam.gymreportservice.dto.TrainingReportResponse;
import com.epam.gymreportservice.service.TrainingReportServiceImpl;
import com.epam.gymservice.dto.request.TrainingReport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ConsumerTest {

    @InjectMocks
    private Consumer listener;
    @Mock
    TrainingReportServiceImpl reportService;
    static TrainingReport trainingReport;
    static TrainingReportResponse response;

    @BeforeAll
    static void setUp() {

        trainingReport = TrainingReport.builder()
                .trainerUsername("sasi")
                .trainerLastName("sasi")
                .trainerFirstName("sasi")
                .date(LocalDate.of(2023, Month.AUGUST,20))
                .duration(20L)
                .trainerIsActive(true)
                .build();

        response = TrainingReportResponse.builder()
                .trainerUsername("sasi")
                .trainerLastName("sasi")
                .trainerFirstName("sasi")
                .durationSummary(new HashMap<>())
                .trainerIsActive(true)
                .build();

    }

    @Test
    void getReportFromServerTest() {
        Mockito.when(reportService.insertTrainingReport(any(TrainingReport.class))).thenReturn(response);
        listener.getReportFromServer(trainingReport);
    }
}
