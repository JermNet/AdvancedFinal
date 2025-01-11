package com.example.personinfo;

import com.example.personinfo.controller.MedicationController;
import com.example.personinfo.controller.PersonController;
import com.example.personinfo.model.Medication;
import com.example.personinfo.model.Person;
import com.example.personinfo.repository.MedicationRepository;
import com.example.personinfo.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// This tests sending valid data using Mockito. This test is successful, so the test works.
@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonController personController;

    @MockBean
    private PersonRepository personRepository;

    @MockBean
    private MedicationController medicationController;

    @MockBean
    private MedicationRepository medicationRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addPerson() throws Exception {
        Person person = new Person("John", "Doe", "1234 Main St", "123-456-7890", 30);
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(post("/personinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createPerson() throws Exception {
        Medication medication = new Medication(1L, "Asprin", "500mg");

        Mockito.when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        Person person = new Person("Mario", "Mario", "Mushroom Kingdom", "555-5555", 35);
        person.setMedication(List.of(medication));

        Mockito.when(personRepository.save(Mockito.any(Person.class))).thenReturn(person);

        mockMvc.perform(post("/personinfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "firstName": "Mario",
                                "lastName": "Mario",
                                "address": "Mushroom Kingdom",
                                "phoneNumber": "555-5555",
                                "age": 35,
                                "medication": [{"id": 1}]
                            }
                        """))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updatePersonWithMedications_ValidRequest_ShouldReturnUpdatedPerson() throws Exception {
        Medication medication = new Medication(1L, "Asprin", "500mg");

        Mockito.when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));

        Person existingPerson = new Person("Mario", "Mario", "Mushroom Kingdom", "555-5555", 35);
        existingPerson.setMedication(List.of(medication));

        Person updatedPerson = new Person("Luigi", "Mario", "Mushroom Kingdom", "555-5555", 35);

        Mockito.when(personRepository.findById(1L)).thenReturn(Optional.of(existingPerson));
        Mockito.when(medicationRepository.findById(1L)).thenReturn(Optional.of(medication));
        Mockito.when(personRepository.save(Mockito.any(Person.class))).thenReturn(updatedPerson);

        mockMvc.perform(put("/personinfo/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "firstName": "Luigi",
                                "lastName": "Mario",
                                "address": "Muhroom Kingdom",
                                "phoneNumber": "555-5555",
                                "age": 35,
                                "medication": [{"id": 1}]
                            }
                        """))
                .andExpect(status().is2xxSuccessful());
    }

}
