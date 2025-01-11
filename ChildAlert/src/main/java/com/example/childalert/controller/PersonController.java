package com.example.childalert.controller;

import com.example.childalert.model.Address;
import com.example.childalert.model.Person;
import com.example.childalert.repository.AddressRepository;
import com.example.childalert.repository.PersonRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// This is the same as address where this mapping is for all methods unless otherwise specified
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;
    private final Logger logger = LoggerFactory.getLogger(PersonController.class);

    /**
     * Get all people using the person repository
     *
     * @return response entity with ok status and the people
     */
    @GetMapping
    public ResponseEntity<List<Person>> getAllPeople() {
        logger.info("Received request to get all people");
        List<Person> people = personRepository.findAll();
        return ResponseEntity.ok(people);
    }

    /**
     * Makes a person, also checking to make sure the address specified does exist before saving
     *
     * @param person the person information to be saved
     *
     * @return a response entity with the person and created status, can also throw an exception
     */
    @PostMapping
    public ResponseEntity<Person> addPerson(@Valid @RequestBody Person person) {
        logger.info("Received request to add a new Person: {}", person);
        Address address = addressRepository.findById(person.getAddressId()).orElseThrow(() -> new RuntimeException("Address not found"));
        person.setAddress(address);
        address.getPeople().add(person);
        Person savedPerson = personRepository.save(person);
        return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
    }

    /**
     * Use id to find a person to update, then use person details as the new information
     *
     * @param id to find person to be updated
     * @param personDetails new information for the person to be updated
     *
     * @return response entity with ok or not found, or throws an error
     */
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @Valid @RequestBody Person personDetails) {
        logger.info("Received request to update Person: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(personDetails.getFirstName());
                    person.setLastName(personDetails.getLastName());
                    person.setAge(personDetails.getAge());
                    person.setPhoneNumber(personDetails.getPhoneNumber());
                    person.setAddressId(personDetails.getAddressId());
                    Address address = addressRepository.findById(personDetails.getAddressId())
                            .orElseThrow(() -> new RuntimeException("Address not found"));
                    person.setAddress(address);
                    Address existingAddress = person.getAddress();
                    // Remove from old address, add to new
                    existingAddress.getPeople().remove(person);
                    existingAddress.getPeople().add(person);
                    Person updatedPerson = personRepository.save(person);
                    return ResponseEntity.ok(updatedPerson);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a person by their id, also making sure to remove them from an address
     *
     * @param id the id to delete a person by
     *
     * @return a response entity with either no content or not found depending on circumstances
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletePerson(@PathVariable Long id) {
        logger.info("Received request to delete Person: {}", id);
        return personRepository.findById(id)
                .map(person -> {
                    Address address = person.getAddress();
                    address.getPeople().remove(person);
                    personRepository.delete(person);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
