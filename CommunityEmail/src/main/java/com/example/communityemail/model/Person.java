package com.example.communityemail.model;

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
    @NotNull(message = "phone number cannot be null!")
    private String phoneNumber;
    @NotNull(message = "email cannot be null!")
    private String email;
    @Positive(message = "age cannot be negative!")
    private int age;

    // A city can have many people but only one person can be in a city at a time so this makes perfect sense
    @ManyToOne
    @JoinColumn(name = "city_id")
    @JsonBackReference
    private City city;

    // This means that this should not persist, using this so I can check if a fire station is attached to a person
    @Transient
    private long cityId;

    // Constructors
    public Person(long id, String firstName, String lastName, String address, String phoneNumber, String email, int age, City city) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.age = age;
        this.city = city;
    }

    public Person(String firstName, String lastName, String address, String phoneNumber, String email, int age, City city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.age = age;
        this.city = city;
    }

    public Person(String firstName, String lastName, String address, String phoneNumber, String email, int age, long cityId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.age = age;
        this.cityId = cityId;
    }

    public Person() {

    }

}

