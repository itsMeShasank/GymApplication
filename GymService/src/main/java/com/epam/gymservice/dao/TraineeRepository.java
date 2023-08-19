package com.epam.gymservice.dao;

import com.epam.gymservice.model.Trainee;
import com.epam.gymservice.model.Training;
import com.epam.gymservice.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<Trainee,Long> {
    Optional<Trainee> findByUserId(Long id);
    @Query("SELECT t FROM Training t WHERE (:from IS NULL OR t.trainingDate >= :from) AND (:to IS NULL OR t.trainingDate < :to) AND (:trainee IS NULL OR t.trainee = :trainee) AND (:trainingType IS NULL OR t.trainingType = :trainingType)")
    List<Training> findAllTrainingInBetween(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("trainee") Trainee trainee, @Param("trainingType") TrainingType trainingType);

}
