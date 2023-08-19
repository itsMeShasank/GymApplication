package com.epam.gymreportservice.service;

import com.epam.gymreportservice.dto.TrainingReportResponse;
import com.epam.gymreportservice.helper.ReportException;
import com.epam.gymservice.dto.request.TrainingReport;

public interface TrainingReportService {
    String SERVICE_METHOD_INVOKED = "method {} invoked in TrainingReportServiceImpl class";
    String SERVICE_METHOD_EXITED = "method {} exited in TrainingReportServiceImpl class";

    TrainingReportResponse insertTrainingReport(TrainingReport trainingReportDetails);
    TrainingReportResponse findTrainingReport(String trainerName) throws ReportException;

}
