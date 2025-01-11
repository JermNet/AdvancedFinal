package com.example.communityemail.controller;

import com.example.communityemail.model.City;
import com.example.communityemail.model.Person;
import com.example.communityemail.repository.CityRepository;
import com.example.communityemail.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This is the same as the fire station where this mapping is for all methods unless otherwise specified
@RestController
@RequestMapping("/communityEmail")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private CityRepository cityRepository;
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    /**
     * Get all people by the name of the city
     *
     * @param city the name of the city within the city object
     *
     * @return a response entity with either not found or ok depending on circumstances
     */
    @GetMapping
    public ResponseEntity<List<Person>> getPeopleByCity(@RequestParam String city) {
        logger.info("Received request to get a new Person: {}", city);
        List<Person> people = personRepository.findByCityName(city);
        if (people.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(people);
        }
    }

    /**
     * Posts a person, making sure to check if the specified city exists or not
     *
     * @param person the person to be posted
     *
     * @return a response entity with the saved person and created status
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        logger.info("Received request to add a new Person: {}", person);
        City city = cityRepository.findById(person.getCityId()).orElseThrow(() -> new RuntimeException("City not found"));
        person.setCity(city);
        city.getPeople().add(person);
        Person savedPerson = personRepository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    /**
     * Update a person by finding them using id and using the new information of person details
     *
     * @param id the id to find the to be updated person
     * @param personDetails the new details to replace the old ones
     *
     * @return a response entity with either ok status or not found depending on circumstances
     */
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personDetails) {
        logger.info("Received request to update a Person: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(personDetails.getFirstName());
                    person.setLastName(personDetails.getLastName());
                    person.setAddress(personDetails.getAddress());
                    person.setPhoneNumber(personDetails.getPhoneNumber());
                    person.setEmail(personDetails.getEmail());
                    person.setAge(personDetails.getAge());
                    person.setCityId(personDetails.getCityId());
                    City city = cityRepository.findById(personDetails.getCityId())
                            .orElseThrow(() -> new RuntimeException("City not found"));
                    person.setCity(city);
                    City existingCity = person.getCity();
                    // Remove from old city, add new
                    existingCity.getPeople().remove(person);
                    existingCity.getPeople().add(person);
                    Person updatedPerson = personRepository.save(person);
                    return ResponseEntity.ok(updatedPerson);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a person by finding them using the person repository and deleting them
     *
     * @param id the id of the person to be deleted
     *
     * @return a response entity with either no content or not found status depending on circumstances
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable Long id) {
        logger.info("Received request to delete a Person: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    City city = person.getCity();
                    city.getPeople().remove(person);
                    personRepository.delete(person);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
