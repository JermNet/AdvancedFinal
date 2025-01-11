package com.example.communityemail.controller;

import com.example.communityemail.model.City;
import com.example.communityemail.repository.CityRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Having the mapping here makes it the mapping for all functions. In a case where there would be 2 or more of the same mapping, I could specify something like "/map" for the method, and, as a result, the entire path would be "/city/map"
@RestController
@RequestMapping("/city")
public class CityController {
    @Autowired
    private CityRepository cityRepository;

    private final Logger logger = LoggerFactory.getLogger(CityController.class);

    /**
     * Gets all cities using the find all method of the city repository
     *
     * @return a response entity with either okay or not found status depending on circumstance
     */
    @GetMapping
    public ResponseEntity<List<City>> getAllCities() {
        logger.info("Received request to get all cities");
        List<City> response = cityRepository.findAll();
        // Return not found if response is empty, return ok if the response is there
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Saves a city to the db using the city repository
     *
     * @param city the city to be saved
     *
     * @return a response entity with the saved city and a created status
     */
    @PostMapping
    public ResponseEntity<City> addCity(@Valid @RequestBody City city) {
        logger.info("Received request to add a new City: {}", city);
        City savedCity = cityRepository.save(city);
        return new ResponseEntity<>(savedCity, HttpStatus.CREATED);
    }

    /**
     * Update a city by finding the old one using id and updating it with the information of city details
     *
     * @param id the id of the city to be updated
     * @param cityDetails the new details of the city
     *
     * @return a response entity with okay or not found depending on the circumstances
     */
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @Valid @RequestBody City cityDetails) {
        logger.info("Received request to update a City: {}", id);
        return cityRepository.findById(id)
                .map(city -> {
                    city.setPopulation(cityDetails.getPopulation());
                    city.setName(cityDetails.getName());
                    city.setAreaCode(cityDetails.getAreaCode());
                    City updatedCity = cityRepository.save(city);
                    return ResponseEntity.ok(updatedCity);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Delete a city using the id provided
     *
     * @param id the id of the city to be deleted
     *
     * @return a response entity with either no content or not found depending on the circumstances
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteCity(@PathVariable Long id) {
        logger.info("Received request to delete a City: {}", id);
        return cityRepository.findById(id)
                .map(city -> {
                    cityRepository.delete(city);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}

