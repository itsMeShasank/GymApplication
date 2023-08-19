package com.epam.gymreportservice.service;

import com.epam.gymreportservice.dao.TrainingReportRepository;
import com.epam.gymreportservice.dto.TrainingReportResponse;
import com.epam.gymreportservice.helper.ReportException;
import com.epam.gymreportservice.model.Report;
import com.epam.gymservice.dto.request.TrainingReport;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportServiceTest {

    @Mock
    TrainingReportRepository trainingReportRepository;

    @InjectMocks
    TrainingReportServiceImpl trainingReportServiceImpl;
    TrainingReport trainingReport;
    Report report;
    TrainingReportResponse response;

    @BeforeEach
    void setupValues() {
         trainingReport = TrainingReport.builder()
                .trainerUsername("sasi")
                .trainerLastName("sasi")
                .trainerFirstName("sasi")
                .date(LocalDate.of(2023,Month.AUGUST,20))
                .duration(20L)
                .trainerIsActive(true)
                .build();
         report = Report.builder()
                .trainerUsername("sasi")
                .trainerLastName("sasi")
                .trainerFirstName("sasi")
                .durationSummary(new HashMap<>())
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
    void insertTrainingReportTest() {

        Mockito.when(trainingReportRepository.findById(any(String.class))).thenReturn(Optional.of(report));
        Mockito.when(trainingReportRepository.save(any(Report.class))).thenReturn(new Report());
        Mockito.when(trainingReportServiceImpl.insertTrainingReport(trainingReport)).thenReturn(response);

    }
    @Test
    void insertTrainingReportFailedTest() {

        Mockito.when(trainingReportRepository.findById(any(String.class))).thenReturn(Optional.empty());
        Mockito.when(trainingReportRepository.save(any(Report.class))).thenReturn(new Report());
        Mockito.when(trainingReportServiceImpl.insertTrainingReport(trainingReport)).thenReturn(response);

    }

    @Test
    void findTrainingReportTest() {

        Mockito.when(trainingReportRepository.findById("sasi")).thenReturn(Optional.of(report));
        assertEquals(response,trainingReportServiceImpl.findTrainingReport("sasi"));
    }

    @Test
    void findTrainingReportReportExceptionTest() {

        Mockito.when(trainingReportRepository.findById(any(String.class))).thenReturn(Optional.empty());
        assertThrows(ReportException.class, () -> {
            trainingReportServiceImpl.findTrainingReport("none");
        });
    }


}
