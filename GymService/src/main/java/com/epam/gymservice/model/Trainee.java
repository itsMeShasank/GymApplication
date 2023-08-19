package com.epam.gymservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Trainee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Date_of_Birth")
    private LocalDate dateOfBirth;

    private String address;

    @OneToOne
    private User user;

    @OneToMany(mappedBy = "trainee",cascade = CascadeType.ALL)
    private List<Training> training;

    @ManyToMany
    private List<Trainer> trainers;
}
