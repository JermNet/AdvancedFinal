package com.example.communityemail;

import com.example.communityemail.controller.PersonController;
import com.example.communityemail.model.City;
import com.example.communityemail.model.Person;
import com.example.communityemail.repository.CityRepository;
import com.example.communityemail.repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
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
    private CityRepository cityRepository;

    @MockBean
    private PersonController personController;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addPerson() throws Exception {
        City city = new City(1, 4, "123 Main St", "Test");
        Person person = new Person("John", "Doe", "1234 Main St", "123-456-7890", "Test", 30, 1);
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(post("/communityEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getPeopleByCity() throws Exception {
        Person person1 = new Person("John", "Doe", "1234 Main St", "123-456-7890", "Test", 30, 1);
        Person person2 = new Person("John", "Doe", "1234 Main St", "123-456-7890", "Test", 30, 2);
        List<Person> people = Arrays.asList(person1, person2);


        Mockito.when(personRepository.findByCityName("Springfield")).thenReturn(people);

        mockMvc.perform(get("/communityEmail")
                        .param("city", "Springfield")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updatePerson() throws Exception {
        City city = new City(1L, 1000, "New York", "333-3333");
        Person existingPerson = new Person("John", "Doe", "1234 Main St", "123-456-7890", "Test", 30, 1);

        Mockito.when(personRepository.findById(1L)).thenReturn(Optional.of(existingPerson));
        Mockito.when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        Person updatedPersonDetails = new Person("Jane", "Doe", "1234 Main St", "123-456-7890", "Test", 30, 1);
        String updatedPersonJson = new ObjectMapper().writeValueAsString(updatedPersonDetails);

        mockMvc.perform(put("/communityEmail/0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedPersonJson))
                .andExpect(status().isOk());
    }

    @Test
    void deletePerson() throws Exception {
        City city = new City(1L, 1000, "New York", "333-3333");
        Person existingPerson = new Person("John", "Doe", "1234 Main St", "123-456-7890", "Test", 30, 1);
        existingPerson.setId(1L);

        Mockito.when(personRepository.findById(1L)).thenReturn(Optional.of(existingPerson));
        Mockito.when(cityRepository.findById(1L)).thenReturn(Optional.of(city));

        mockMvc.perform(delete("/communityEmail/1"))
                .andExpect(status().isOk());
    }

}
