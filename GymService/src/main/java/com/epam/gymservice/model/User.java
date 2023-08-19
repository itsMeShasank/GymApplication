package com.epam.gymservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "username",unique = true)
    private String userName;
    @Column(unique = true)
    private String mail;
    @Column(name = "password")
    private String password;

    private boolean isActive = true;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Trainee trainee;

    @OneToOne(mappedBy = "user",cascade = CascadeType.ALL)
    private Trainer trainer;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userName='" + userName + '\'' +
                ", mail='" + mail + '\'' +
                ", password='" + password + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}
