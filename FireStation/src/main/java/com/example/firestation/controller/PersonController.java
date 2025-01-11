package com.example.firestation.controller;

import com.example.firestation.model.FireStation;
import com.example.firestation.model.Person;
import com.example.firestation.repository.FireStationRepository;
import com.example.firestation.repository.PersonRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This is the same as the fire station where this mapping is for all methods unless otherwise specified
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FireStationRepository fireStationRepository;
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    /**
     * Gets all people from the person repository using finaAll
     *
     * @return a response entity with all people and an okay status
     */
    @GetMapping
    public ResponseEntity<List<Person>> getAllPeople() {
        logger.info("Received request to get all people");
        List<Person> people = personRepository.findAll();
        return ResponseEntity.ok(people);
    }

    /**
     * Saves a person to the db, also adding them to a fire station.
     *
     * @param person the person to be saved to the db
     *
     * @return a response entity with the person and created status, or throws an error if the fire station doesn't exist
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody @Valid Person person) {
        logger.info("Received request to add a new Person: {}", person);
        // Use this to add a person. First get the fire station (if it exists), set the fire station to the person, add the person to the fire station, save the person to the db and then return status created.
        FireStation fireStation = fireStationRepository.findById(person.getFireStationId()).orElseThrow(() -> new RuntimeException("FireStation not found"));
        person.setFireStation(fireStation);
        fireStation.getPeople().add(person);
        Person savedPerson = personRepository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    /**
     * Updates a person with new details using an id to find the old person to replace, and Person for the new information
     *
     * @param id is the id of the person that will be updated
     * @param personDetails is the new information that will replace the old
     *
     * @return response entity with not found or ok and the update person depending on the circumstances
     */
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody @Valid Person personDetails) {
        logger.info("Received request to update a person with id: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(personDetails.getFirstName());
                    person.setLastName(personDetails.getLastName());
                    person.setAddress(personDetails.getAddress());
                    person.setPhoneNumber(personDetails.getPhoneNumber());
                    person.setAge(personDetails.getAge());
                    person.setFireStationId(personDetails.getFireStationId());
                    FireStation fireStation = fireStationRepository.findById(personDetails.getFireStationId())
                            .orElseThrow(() -> new RuntimeException("Fire station not found"));
                    person.setFireStation(fireStation);
                    FireStation existingFireStation = person.getFireStation();
                    // Remove from old fire station, add to new
                    existingFireStation.getPeople().remove(person);
                    existingFireStation.getPeople().add(person);
                    Person updatedPerson = personRepository.save(person);
                    return ResponseEntity.ok(updatedPerson);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a person using an id, using the personRepository to find that person in the first place. Also removes them from their fire station
     *
     * @param id the id of the person to bee deleted
     *
     * @return a response entity with either no content or not found depending on the circumstance
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable Long id) {
        logger.info("Received request to delete a person with id: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    FireStation fireStation = person.getFireStation();
                    fireStation.getPeople().remove(person);
                    personRepository.delete(person);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

