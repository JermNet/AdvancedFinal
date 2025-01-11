package com.example.childalert.controller;

import com.example.childalert.model.Address;
import com.example.childalert.repository.AddressRepository;
import com.example.childalert.service.PersonService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Having the mapping here makes it the mapping for all functions. In a case where there would be 2 or more of the same mapping, I could specify something like "/map" for the method, and, as a result, the entire path would be "/address/map"
@RestController
@RequestMapping("/childAlert")
public class AddressController {
    @Autowired
    private PersonService personService;
    @Autowired
    private AddressRepository addressRepository;
    private final Logger logger = LoggerFactory.getLogger(AddressController.class);

    // Initialize the service so we can use it
    public AddressController(PersonService personService) {
        this.personService = personService;
    }

    /**
     * Gets people by address in a specific format using the person service
     *
     * @param address the address to get people by, a string address
     *
     * @return a response entity with not found or ok depending on the circumstances
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPeopleByAddress(@RequestParam("address") String address) {
        logger.info("Received request to get people by address: {}", address);
        // Here I use a Map, since the keys can't be the same which works really nicely for this
        Map<String, Object> response = personService.getPeopleByAddress(address);
        // Return not found if response is empty, return ok if the response is there.
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Saves an address to the db using the addressRepository
     *
     * @param address the information of the address that's to be saved
     *
     * @return a response entity with the address and created status
     */
    @PostMapping
    public ResponseEntity<Address> addAddress(@RequestBody @Valid Address address) {
        logger.info("Received request to add a new Address: {}", address);
        Address savedaddress = addressRepository.save(address);
        return new ResponseEntity<>(savedaddress, HttpStatus.CREATED);
    }

    /**
     * Updates an old address by specifying it with the id, and using the new information of addressDetails
     *
     * @param id used to get the address to be updated
     * @param addressDetails used for the new information
     *
     * @return response entity with ok or not found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody @Valid Address addressDetails) {
        logger.info("Received request to update Address: {}", id);
        return addressRepository.findById(id)
                .map(address -> {
                    address.setAddress(addressDetails.getAddress());
                    Address updatedAddress = addressRepository.save(address);
                    return ResponseEntity.ok(updatedAddress);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes an address by its id
     *
     * @param id the id of the address to be deleted
     *
     * @return a response entity with either no content or not found depending on the circumstances
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAddress(@PathVariable Long id) {
        logger.info("Received request to delete Address: {}", id);
        return addressRepository.findById(id)
                .map(address -> {
                    addressRepository.delete(address);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}