package com.example.childalert.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name="AddressTable")
public class Address {

    // Setters and getters
    // Address ID
    @Id
    @GeneratedValue
    private long id;

    // String with the address. This is all a separate table so it works better in a database rather than each person having their own string address in which there would be copied addresses.
    @NotNull(message = "address cannot be null!")
    private String address;

    // One to many since a person should only have one address, but an address can have many people. Json thing is to avoid json issues
    @OneToMany(mappedBy = "address", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Person> people;

    // Constructors
    public Address(long id, String address, List<Person> people) {
        this.id = id;
        this.address = address;
        this.people = people;
    }

    public Address(String address, List<Person> people) {
        this.address = address;
        this.people = people;
    }

    public Address(long id, String address) {
        this.id = id;
        this.address = address;
    }

    public Address() {

    }

}
