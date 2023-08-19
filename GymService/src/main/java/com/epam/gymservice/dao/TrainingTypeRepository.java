package com.epam.gymservice.dao;

import com.epam.gymservice.model.TrainingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingType,Long> {
    Optional<TrainingType> findBySpecialization(String specialization);
    void deleteBySpecialization(String trainingType);
}
