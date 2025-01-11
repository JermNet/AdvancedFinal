package com.example.personinfo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name="PersonTable")
public class Person {

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

    // Make a table for this since it's more than one value.
    @ElementCollection
    @CollectionTable(name = "Allergies", joinColumns = @JoinColumn(name = "person_id"))
    @Column(name = "allergy")
    private List<String> allergies;

    // This is many to many since one person can have more than one medication and vise versa.
    @ManyToMany(cascade = CascadeType.MERGE)
    private List<Medication> medication;

    // Constructors
    public Person(long id, String firstName, String lastName, String address, String phoneNumber, int age, List<String> allergies, List<Medication> medication) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.allergies = allergies;
        this.medication = medication;
    }

    public Person(String firstName, String lastName, String address, String phoneNumber, int age, List<String> allergies, List<Medication> medication) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.allergies = allergies;
        this.medication = medication;
    }

    public Person(String firstName, String lastName, String address, String phoneNumber, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }

    public Person() {

    }

}
