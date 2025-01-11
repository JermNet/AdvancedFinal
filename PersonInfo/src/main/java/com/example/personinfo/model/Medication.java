package com.example.personinfo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name="MedicationTable")
public class Medication {

    // Setters and getters
    // Medication ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // Name and dosage. Dosage is a string so the amount in number and measurement can be specified.
    @NotNull(message = "name cannot be null!")
    private String name;
    @NotNull(message = "dosage cannot be null!")
    private String dosage;

    // Medication is many to many since one person can have more than one medication and vise versa. Json thing is to avoid json issues
    @ManyToMany(mappedBy = "medication")
    @JsonIgnore
    private List<Person> people;

    // Constructors
    public Medication(long id, String name, String dosage, List<Person> people) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
        this.people = people;
    }

    public Medication(String name, String dosage, List<Person> people) {
        this.name = name;
        this.dosage = dosage;
        this.people = people;
    }

    public Medication(long id, String name, String dosage) {
        this.id = id;
        this.name = name;
        this.dosage = dosage;
    }

    public Medication() {

    }

}

