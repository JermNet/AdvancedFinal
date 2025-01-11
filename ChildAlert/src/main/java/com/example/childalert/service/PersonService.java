package com.example.childalert.service;

import com.example.childalert.model.Address;
import com.example.childalert.model.Person;
import com.example.childalert.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
@Service
public class PersonService implements PersonServiceInterface {
    private PersonRepository personRepository;

    // Can have the constructor be autowired so it autowires everything in the constructor
    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // Use a Map so the keys of the key value pair are unique
    @Override
    public Map<String, Object> getPeopleByAddress(String address) {
        List<Person> people = personRepository.findByAddressAddress(address);

        // If the Map is empty, return an empty Map.
        if (people.isEmpty()) {
            return Collections.emptyMap();
        }

        // List of people
        List<Map<String, String>> personInfo = new ArrayList<>();

        for (Person person : people) {
            // Get the people only if they are under 18
            Map<String, String> personData = new HashMap<>();
            if (person.getAge() < 18) {
                personData.put("firstName", person.getFirstName());
                personData.put("lastName", person.getLastName());
                personData.put("phone", person.getPhoneNumber());
                personData.put("age", String.valueOf(person.getAge()));
            }
            personInfo.add(personData);
        }

        // Add everything to a response so it can easily be returned
        Map<String, Object> response = new HashMap<>();
        response.put("people", personInfo);
        return response;
    }
}
