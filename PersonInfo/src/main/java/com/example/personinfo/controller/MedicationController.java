package com.example.personinfo.controller;

import com.example.personinfo.model.Medication;
import com.example.personinfo.model.Person;
import com.example.personinfo.repository.MedicationRepository;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Having the mapping here makes it the mapping for all functions. In a case where there would be 2 or more of the same mapping, I could specify something like "/map" for the method, and, as a result, the entire path would be "/medication/map"
@RestController
@RequestMapping("/medication")
public class MedicationController {
    @Autowired
    private MedicationRepository medicationRepository;
    private final Logger logger = LoggerFactory.getLogger(MedicationController.class);

    /**
     * Uses the medication repository to find all medication to be returned
     *
     * @return a response entity wth either okay or not found status depending on the circumstances
     */
    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedication() {
        logger.info("Received request to get all Medication");
        List<Medication> response = medicationRepository.findAll();
        // Return not found if response is empty, return ok if the response is there
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Take medication as a request, then save it to the db using the medication repository
     *
     * @param medication the medication to be saved
     *
     * @return a response entity with the medication and created status
     */
    @PostMapping
    public ResponseEntity<Medication> addMedication(@Valid @RequestBody Medication medication) {
        logger.info("Received request to add a new Medication: {}", medication);
        Medication savedMedication = medicationRepository.save(medication);
        return new ResponseEntity<>(savedMedication, HttpStatus.CREATED);
    }

    /**
     * Use the id to update an old medication with the new medication details
     *
     * @param id the id of the medication to be updated
     * @param medicationDetails the new details of the medication to be updated
     *
     * @return a response entity with either ok or not found status depending on circumstances
     */
    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(@PathVariable Long id, @Valid @RequestBody Medication medicationDetails) {
        logger.info("Received request to update a Medication: {}", id);
        return medicationRepository.findById(id)
                .map(medication -> {
                    if (medication.getPeople() != null) {
                        for (Person person : medication.getPeople()) {
                            person.getMedication().remove(medication);
                        }
                    }
                    medication.setName(medicationDetails.getName());
                    medication.setDosage(medicationDetails.getDosage());
                    Medication updatedMedication = medicationRepository.save(medication);
                    return ResponseEntity.ok(updatedMedication);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
