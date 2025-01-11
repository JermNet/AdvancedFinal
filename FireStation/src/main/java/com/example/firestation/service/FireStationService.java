package com.example.firestation.service;

import com.example.firestation.model.Person;
import com.example.firestation.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

// Use this service to get people based on the station they've been served by.
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
public class FireStationService implements FireStationServiceInterface{

    private PersonRepository personRepository;

    // Can have the constructor be autowired so it autowires everything in the constructor
    @Autowired
    public FireStationService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // Use a Map so the keys of the key value pair are unique
    @Override
    public Map<String, Object> getPeopleByStation(int stationNumber) {
        List<Person> people = personRepository.findByFireStationStationNumber(stationNumber);

        // If the Map is empty, return an empty Map.
        if (people.isEmpty()) {
            return Collections.emptyMap();
        }

        // Ints for number of children and adults that have been served.
        int adultCount = 0;
        int childCount = 0;
        List<Map<String, String>> personInfo = new ArrayList<>();

        for (Person person : people) {
            // Get the people
            Map<String, String> personData = new HashMap<>();
            personData.put("firstName", person.getFirstName());
            personData.put("lastName", person.getLastName());
            personData.put("address", person.getAddress());
            personData.put("phone", person.getPhoneNumber());

            // Increase the respective counts for the people
            if (person.getAge() > 18) {
                adultCount++;
            } else {
                childCount++;
            }
            // Add the data to the Map List
            personInfo.add(personData);
        }

        // Add everything to a response so it can easily be returned
        Map<String, Object> response = new HashMap<>();
        response.put("people", personInfo);
        response.put("adultCount", adultCount);
        response.put("childCount", childCount);

        return response;
    }
}

