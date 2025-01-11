package com.example.firestation;

import com.example.firestation.controller.PersonController;
import com.example.firestation.model.FireStation;
import com.example.firestation.model.Person;
import com.example.firestation.repository.FireStationRepository;
import com.example.firestation.repository.PersonRepository;
import com.example.firestation.service.FireStationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// This tests sending valid data using Mockito. This test is successful, so the test works.
@WebMvcTest
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository fireStationRepository;

    @MockBean
    private FireStationService fireStationService;

    @MockBean
    private PersonController personController;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addPerson() throws Exception {
        FireStation fireStation = new FireStation(1, 4, "123 Main St");
        Person person = new Person("John", "Doe", "1234 Main St", "123-456-7890", 30, 1);
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAllPeople() throws Exception {
        FireStation fireStation = new FireStation(1, 4, "123 Main St");
        when(fireStationRepository.save(any(FireStation.class))).thenReturn(fireStation);
        when(fireStationRepository.findById(1L)).thenReturn(Optional.of(fireStation));

        Person person1 = new Person("John", "Doe", "1234 Main St", "123-456-7890", 30, 1);
        Person person2 = new Person("Jane", "Doe", "1234 Not Main St", "123-456-7891", 30, 1);

        when(personRepository.findAll()).thenReturn(Arrays.asList(person1, person2));

        mockMvc.perform(get("/person"))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePerson() throws Exception {
        long personId = 0L;
        long fireStationId = 2L;

        FireStation fireStation = new FireStation(2L, 5, "456 Elm St");
        Person person = new Person( "John", "Doe", "123 Main St", "555-5555", 30, fireStationId);
        person.setFireStation(fireStation);

        Person updatedPersonDetails = new Person("John", "Smith", "123 New Main St", "555-5556", 35, fireStationId);

        when(personRepository.findById(personId)).thenReturn(java.util.Optional.of(person));
        when(fireStationRepository.findById(fireStationId)).thenReturn(java.util.Optional.of(fireStation));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPersonDetails);

        mockMvc.perform(put("/person/{id}", personId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonDetails)))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePerson() throws Exception {
        long personId = 1L;
        long fireStationId = 2L;

        FireStation fireStation = new FireStation(fireStationId, 5, "456 Elm St");
        Person person = new Person( "John", "Doe", "123 Main St", "555-5555", 30, fireStationId);
        person.setFireStation(fireStation);

        when(personRepository.findById(personId)).thenReturn(java.util.Optional.of(person));

        mockMvc.perform(delete("/person/{id}", personId))
                .andExpect(status().isOk());
    }


}
