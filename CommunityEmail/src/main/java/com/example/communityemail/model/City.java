package com.example.communityemail.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name="CityTable")
public class City {

    // Setters and getters
    // City ID
    @Id
    @GeneratedValue
    private long id;

    // Things for a city. Population isn't the count of how many people are in the people list, it's a more general number. Like how in real life where a population isn't perfectly exact.
    @Positive(message = "population cannot be negative!")
    private int population;
    @NotNull(message = "name cannot be null!")
    private String name;
    @NotNull(message = "areaCode cannot be null!")
    private String areaCode;

    // Realistically, one person can't be in more city than one at a time, so this makes the most sense. Cascade and orphanRemoval are to prevent db issues when a city is deleted but a person is not.
    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Person> people;

    // Constructors
    public City(long id, int population, String name, String areaCode, List<Person> people) {
        this.id = id;
        this.population = population;
        this.name = name;
        this.areaCode = areaCode;
        this.people = people;
    }

    public City(int population, String name, String areaCode, List<Person> people) {
        this.population = population;
        this.name = name;
        this.areaCode = areaCode;
        this.people = people;
    }

    public City(long id, int population, String name, String areaCode) {
        this.id = id;
        this.population = population;
        this.name = name;
        this.areaCode = areaCode;
    }

    public City() {

    }

}

