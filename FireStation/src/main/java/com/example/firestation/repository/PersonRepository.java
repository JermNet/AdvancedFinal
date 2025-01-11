package com.example.firestation.repository;

import com.example.firestation.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// Repository for People, uses JPA built in methods to get first station station number.
public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByFireStationStationNumber(int stationNumber);
    List<Person> findByFirstNameAndLastName(String firstName, String lastName);
}
