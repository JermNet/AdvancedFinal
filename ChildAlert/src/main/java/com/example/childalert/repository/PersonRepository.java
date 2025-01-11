package com.example.childalert.repository;

import com.example.childalert.model.Address;
import com.example.childalert.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByAddressAddress(String address);
}
