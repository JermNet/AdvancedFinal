package com.example.personinfo.controller;

import com.example.personinfo.model.Medication;
import com.example.personinfo.model.Person;
import com.example.personinfo.repository.MedicationRepository;
import com.example.personinfo.repository.PersonRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This is the same as the medication where this mapping is for all methods unless otherwise specified
@RestController
@RequestMapping("/personinfo")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private MedicationRepository medicationRepository;
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    /**
     * Find a person or people by both of their names using the person repository
     *
     * @param firstName the first name of the person or people to be found
     * @param lastName the last name of the person or people to be found
     *
     * @return a list of people that have the firstName and lastName
     */
    @GetMapping
    public ResponseEntity<List<Person>> getPeopleByName(@RequestParam String firstName, @RequestParam String lastName) {
        logger.info("Received request to add find people: {} {}", firstName, lastName);
        List<Person> people = personRepository.findByFirstNameAndLastName(firstName, lastName);
        if (people.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(people);
        }
    }

    /**
     * Adds a person to the db using the person repository
     *
     * @param person the person to be saved into the db
     *
     * @return a response entity with the saved person and created status or bad request depending on the circumstance
     */
    @PostMapping
    public ResponseEntity<Person> createPerson(@Valid @RequestBody Person person) {
        logger.info("Received request to add a new Person: {}", person);
        for (Medication med : person.getMedication()) {
            if (medicationRepository.findById(med.getId()).isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
        }
        Person savedPerson = personRepository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }


    /**
     * Updates a person using id and new person details
     *
     * @param id the id of the person to be updated
     * @param personDetails the details of the new person
     *
     * @return a response entity with okay or not found, can also throw an error
     */
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePersonWithMedications(@PathVariable Long id, @Valid @RequestBody Person personDetails) {
        logger.info("Received request to update a Person: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(personDetails.getFirstName());
                    person.setLastName(personDetails.getLastName());
                    person.setAddress(personDetails.getAddress());
                    person.setPhoneNumber(personDetails.getPhoneNumber());
                    person.setAge(personDetails.getAge());

                    // Clear previous medications and then loop through adding the ones in the request
                    if (personDetails.getMedication() != null) {
                        // Clear current medications
                        person.getMedication().clear();

                        // Loop through the medications from the request and set them
                        for (Medication medication : personDetails.getMedication()) {
                            Medication existingMedication = medicationRepository.findById(medication.getId())
                                    .orElseThrow(() -> new RuntimeException("Medication not found"));
                            person.getMedication().add(existingMedication);
                        }
                    }

                    // Save the person
                    Person updatedPerson = personRepository.save(person);
                    return ResponseEntity.ok(updatedPerson);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
