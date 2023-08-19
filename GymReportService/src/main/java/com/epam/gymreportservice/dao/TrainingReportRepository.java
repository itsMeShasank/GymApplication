package com.epam.gymreportservice.dao;

import com.epam.gymreportservice.model.Report;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingReportRepository extends MongoRepository<Report,String> {
}
