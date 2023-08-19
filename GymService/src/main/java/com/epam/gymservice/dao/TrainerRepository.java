package com.epam.gymservice.dao;

import com.epam.gymservice.model.Trainee;
import com.epam.gymservice.model.Trainer;
import com.epam.gymservice.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer,Long> {
    Optional<Trainer> findById(Long id);
    @Query("SELECT t FROM Trainer t WHERE t NOT IN :trainerList")
    List<Trainer> findTrainersNotInList(@Param("trainerList") List<Trainer> trainerList);

    @Query("SELECT t FROM Training t WHERE (:from IS NULL OR t.trainingDate >= :from) AND (:to IS NULL OR t.trainingDate < :to) AND (:trainer IS NULL OR t.trainer = :trainer) AND (:trainingType IS NULL OR t.trainee = :trainingType)")
    List<Training> findAllTrainingInBetween(@Param("from") LocalDate from, @Param("to") LocalDate to, @Param("trainer") Trainer trainer, @Param("trainingType") Trainee trainingType);
}
