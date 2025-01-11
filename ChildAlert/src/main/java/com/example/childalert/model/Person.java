package com.example.childalert.model;

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
    @NotNull(message = "phoneNumber cannot be null!")
    private String phoneNumber;
    @Positive(message = "age cannot be negative!")
    private int age;

    // This links to an address, a person should really only live at one address so this is many to one. Json thing is to avoid json issues
    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonBackReference
    private Address address;

    // This means that this should not persist, using this so I can check if an address is attached to a person
    @Transient
    private long addressId;

    // Constructors
    public Person(long id, String firstName, String lastName, String phoneNumber, int age, Address address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
    }

    public Person(String firstName, String lastName, String phoneNumber, int age, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.address = address;
    }

    public Person(String firstName, String lastName, String phoneNumber, int age, long addressId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.addressId = addressId;
    }

    public Person() {

    }

}
