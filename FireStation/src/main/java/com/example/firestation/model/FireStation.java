package com.example.firestation.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name="FireStationTable")
public class FireStation {

    // Setters and getters
    // Fire station ID
    @Id
    @GeneratedValue
    private long id;

    // Station number and address
    @NotNull(message = "stationNumber cannot be null!")
    private int stationNumber;
    @NotNull(message = "address cannot be null!")
    private String address;

    // Instead of having this be many to many, this is one to many since a fire station can serve many people, but, realistically, a person wouldn't be served by many different fire stations if they're living in one area. Json thing is to avoid json issues
    @OneToMany(mappedBy = "fireStation", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Person> people;

    // Constructors
    public FireStation(long id, int stationNumber, String address, List<Person> people) {
        this.id = id;
        this.stationNumber = stationNumber;
        this.address = address;
        this.people = people;
    }
    
    public FireStation(int stationNumber, String address, List<Person> people) {
        this.stationNumber = stationNumber;
        this.address = address;
        this.people = people;
    }

    public FireStation(long id, int stationNumber, String address) {
        this.id = id;
        this.stationNumber = stationNumber;
        this.address = address;
    }

    public FireStation() {

    }
}
