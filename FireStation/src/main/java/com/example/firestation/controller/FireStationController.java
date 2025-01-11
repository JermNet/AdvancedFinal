package com.example.firestation.controller;

import com.example.firestation.model.FireStation;
import com.example.firestation.repository.FireStationRepository;
import com.example.firestation.service.FireStationService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

// Having the mapping here makes it the mapping for all functions. In a case where there would be 2 or more of the same mapping, I could specify something like "/map" for the method, and, as a result, the entire path would be "/firestation/map"
@RestController
@RequestMapping("/firestation")
public class FireStationController {
    @Autowired
    private FireStationService fireStationService;
    @Autowired
    private FireStationRepository fireStationRepository;
    private final Logger logger = LoggerFactory.getLogger(FireStationController.class);

    /**
     * Takes the fireStation service and initializes it
     *
     * @param fireStationService is used to make an altered get mapping with quantities of people by age
     */
    public FireStationController(FireStationService fireStationService) {
        this.fireStationService = fireStationService;
    }

    /**
     * Takes a stationNumber, and uses the service class to find and get information with that
     *
     * @param stationNumber is used to find people by this number
     *
     * @return an OK response entity or not found with response depending on if information is found
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> getPeopleByStation(@RequestParam("stationNumber") int stationNumber) {
        logger.info("Received a request to get people by fire station {}", stationNumber);
        // Here I use a Map, since the keys can't be the same which works really nicely for this
        Map<String, Object> response = fireStationService.getPeopleByStation(stationNumber);

        // Return not found if response is empty, return ok if the response is there.
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * This takes a FireStation as it's request body, then saving it into the db and returning the FireStation with a CREATED status in a ResponseEntity
     *
     * @param fireStation the (@code FireStation) object that will be added to the db
     *
     * @return a (@code ResponseEntity) that that contains the fireStation as well as a CREATED HttpStatus.
     */
    @PostMapping
    public ResponseEntity<FireStation> addFireStation(@RequestBody @Valid FireStation fireStation) {
        // Save to db, and then return HttpStatus to let know it worked
        logger.info("Received request to add a new FireStation: {}", fireStation);
        FireStation savedFireStation = fireStationRepository.save(fireStation);
        return new ResponseEntity<>(savedFireStation, HttpStatus.CREATED);
    }

    /**
     * Updates a fire station by taking an id and a fire station that contains the new information
     *
     * @param id the id of the old fire station to be updated
     * @param fireStationDetails the updated information in the form of a fire station
     *
     * @return a not found or ok ResponseEntity depending on the circumstances
     */
    @PutMapping("/{id}")
    public ResponseEntity<FireStation> updateFireStation(@PathVariable Long id, @RequestBody @Valid FireStation fireStationDetails) {
        logger.info("Received request to update a FireStation: {}", fireStationDetails);
        return fireStationRepository.findById(id)
                .map(fireStation -> {
                    fireStation.setStationNumber(fireStationDetails.getStationNumber());
                    fireStation.setAddress(fireStationDetails.getAddress());
                    FireStation updatedFireStation = fireStationRepository.save(fireStation);
                    return ResponseEntity.ok(updatedFireStation);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a fire station by taking an id and seeing if it exists in the db
     *
     * @param id the id of the fire station to be deleted
     *
     * @return a response entity with either no content or not found depending on the circumstances
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFireStation(@PathVariable Long id) {
        logger.info("Received request to delete fire station by id: {}", id);
        return fireStationRepository.findById(id)
                .map(fireStation -> {
                    fireStationRepository.delete(fireStation);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

}
