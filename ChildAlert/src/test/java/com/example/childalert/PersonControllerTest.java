package com.example.childalert;

import com.example.childalert.controller.PersonController;
import com.example.childalert.model.Address;
import com.example.childalert.model.Person;
import com.example.childalert.repository.AddressRepository;
import com.example.childalert.repository.PersonRepository;
import com.example.childalert.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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
    private AddressRepository addressRepository;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonController personController;

    @MockBean
    private PersonRepository personRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addPerson_SuccessfulCreation() throws Exception {
        Address address = new Address(1, "123 Main St");
        Person person = new Person("John", "Doe", "123-456-7890", 30, 1);
        String personJson = objectMapper.writeValueAsString(person);

        mockMvc.perform(post("/person")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(personJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAllPeople() throws Exception {
        Person person1 = new Person("John", "Doe", "123-456-7890", 30, 1);
        Person person2 = new Person("Jane", "Doe", "123-456-7891", 30, 1);

        List<Person> people = Arrays.asList(person1, person2);

        when(personRepository.findAll()).thenReturn(people);

        mockMvc.perform(get("/person")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePerson() throws Exception {
        Person existingPerson = new Person("John", "Doe", "123-456-7890", 30, 1);
        existingPerson.setId(1L);

        Address existingAddress = new Address(1L, "1234 Main St");
        Person updatedPersonDetails = new Person("John", "Doe", "987-654-3210", 31, 1);

        when(personRepository.findById(1L)).thenReturn(java.util.Optional.of(existingPerson));
        when(addressRepository.findById(1L)).thenReturn(java.util.Optional.of(existingAddress));
        when(personRepository.save(existingPerson)).thenReturn(existingPerson);

        mockMvc.perform(put("/person/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPersonDetails)))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePerson() throws Exception {
        long personId = 1L;
        long addressId = 2L;

        Address address = new Address(addressId, "456 Elm St");
        Person person = new Person("John", "Doe", "555-5555", 30, addressId);
        person.setAddress(address);

        when(personRepository.findById(personId)).thenReturn(java.util.Optional.of(person));

        mockMvc.perform(delete("/person/{id}", personId))
                .andExpect(status().isOk());

    }
}