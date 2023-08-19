package com.epam.gymservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TrainingType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String specialization;

    @OneToMany(mappedBy = "trainingType",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Training> training;

    @OneToMany(mappedBy = "specialization",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Trainer> trainers;

    @Override
    public String toString() {
        return "TrainingType{" +
                "id=" + id +
                ", trainingType='" + specialization + '\'' +
                ", training=" + training +
                '}';
    }
}
