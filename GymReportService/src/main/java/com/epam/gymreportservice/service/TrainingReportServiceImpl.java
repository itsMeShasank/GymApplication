package com.epam.gymreportservice.service;

import com.epam.gymreportservice.dao.TrainingReportRepository;
import com.epam.gymreportservice.dto.TrainingReportResponse;
import com.epam.gymreportservice.helper.ReportException;
import com.epam.gymreportservice.model.Report;
import com.epam.gymservice.dto.request.TrainingReport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrainingReportServiceImpl implements TrainingReportService{

    private final TrainingReportRepository trainingReportRepository;
    @Override
    public TrainingReportResponse insertTrainingReport(TrainingReport trainingReportDetails) {
        log.info(SERVICE_METHOD_INVOKED,"generateTrainingReport");

        Report report = trainingReportRepository.findById(trainingReportDetails.getTrainerUsername())
                .orElseGet(() -> new Report(
                trainingReportDetails.getTrainerUsername(),
                trainingReportDetails.getTrainerFirstName(),
                trainingReportDetails.getTrainerLastName(),
                trainingReportDetails.isTrainerIsActive(),
                new HashMap<>()));

        LocalDate trainingDate = trainingReportDetails.getDate();

        long year = trainingDate.getYear();
        long month = trainingDate.getMonthValue();
        long day = trainingDate.getDayOfMonth();
        long trainingDuration = trainingReportDetails.getDuration();

        report.getDurationSummary()
                .computeIfAbsent(year, k -> new HashMap<>())
                .computeIfAbsent(month, k -> new HashMap<>())
                .computeIfAbsent(day, k -> new HashMap<>())
                .put(trainingDate.toString(), trainingDuration);

        Report reportDetails = trainingReportRepository.save(report);
        log.info(SERVICE_METHOD_EXITED,"generateTrainingReport");
        return setValuesToReportResponse(reportDetails);
    }

    @Override
    public TrainingReportResponse findTrainingReport(String trainerUsername) throws ReportException {
        log.info(SERVICE_METHOD_INVOKED,"findTrainingReport");
        Report report = trainingReportRepository.findById(trainerUsername)
                .orElseThrow(() -> new ReportException("No Report generated for given trainer username", HttpStatus.BAD_REQUEST));

        TrainingReportResponse reportResponse = new TrainingReportResponse();
        reportResponse.setTrainerUsername(report.getTrainerUsername());
        reportResponse.setTrainerFirstName(report.getTrainerFirstName());
        reportResponse.setTrainerLastName(report.getTrainerLastName());
        reportResponse.setTrainerIsActive(report.isTrainerIsActive());
        reportResponse.setDurationSummary(report.getDurationSummary());
        log.info(SERVICE_METHOD_EXITED,"findTrainingReport");
        return reportResponse;
    }
    private TrainingReportResponse setValuesToReportResponse(Report reportDetails) {
        log.info(SERVICE_METHOD_INVOKED,"setValuesToReportResponse");
        TrainingReportResponse trainingReportResponse = new TrainingReportResponse();
        trainingReportResponse.setTrainerUsername(reportDetails.getTrainerUsername());
        trainingReportResponse.setTrainerFirstName(reportDetails.getTrainerFirstName());
        trainingReportResponse.setTrainerLastName(reportDetails.getTrainerLastName());
        trainingReportResponse.setDurationSummary(reportDetails.getDurationSummary());
        trainingReportResponse.setTrainerIsActive(reportDetails.isTrainerIsActive());
        log.info(SERVICE_METHOD_EXITED,"setValuesToReportResponse");
        return trainingReportResponse;
    }
}

