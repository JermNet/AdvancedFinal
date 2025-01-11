package com.example.firestation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="PersonTable")
public class Person {

    // Setters and getters
    // Person ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "firstName cannot be null!")
    private String firstName;
    @NotNull(message = "lastName cannot be null!")
    private String lastName;
    @NotNull(message = "address cannot be null!")
    private String address;
    @NotNull(message = "phoneNumber cannot be null!")
    private String phoneNumber;
    @Positive(message = "age cannot be negative!")
    private int age;

    // Same as FireStation, this is not ManyToMany since a person in an area would most likely only be served by one fire station. Json thing is to avoid json issues
    @ManyToOne
    @JoinColumn(name = "fireStation_id")
    @JsonBackReference
    private FireStation fireStation;

    // This means that this should not persist, using this, so I can check if a fire station is attached to a person
    @Transient
    private long fireStationId;

    // Constructors
    public Person(long id, String firstName, String lastName, String address, String phoneNumber, int age, FireStation fireStation) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.fireStation = fireStation;
    }

    public Person(String firstName, String lastName, String address, String phoneNumber, int age, FireStation fireStation) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.fireStation = fireStation;
    }

    public Person(String firstName, String lastName, String address, String phoneNumber, int age, long fireStationId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.fireStationId = fireStationId;
    }

    public Person() {

    }

}
