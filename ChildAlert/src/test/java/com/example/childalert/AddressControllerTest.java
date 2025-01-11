package com.example.childalert;

import com.example.childalert.controller.PersonController;
import com.example.childalert.model.Address;
import com.example.childalert.model.Person;
import com.example.childalert.repository.AddressRepository;
import com.example.childalert.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// This tests sending valid data using Mockito. This test is successful, so the test works.
@WebMvcTest
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Despite saying they're not being used, I do still need these
    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private PersonService personService;

    @MockBean
    private PersonController personController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addAddress() throws Exception {
        Address address = new Address(1, "123 Main St");

        String json = objectMapper.writeValueAsString(address);

        mockMvc.perform(post("/childAlert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    public void getPeopleByAddress() throws Exception {
        Person person1 = new Person("John", "Doe", "555-5555", 30, 1);
        Person person2 = new Person("Jane", "Doe", "555-5556", 28, 1);
        Map<String, Object> response = new HashMap<>();
        response.put("people", Arrays.asList(person1, person2));

        when(personService.getPeopleByAddress("123 Main St")).thenReturn(response);

        mockMvc.perform(get("/childAlert")
                        .param("address", "123 Main St")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.people[0].firstName").value("John"))
                .andExpect(jsonPath("$.people[1].firstName").value("Jane"));

        verify(personService).getPeopleByAddress("123 Main St");
    }

    @Test
    public void updateAddress() throws Exception {
        Address existingAddress = new Address(0L, "123 Main St");
        Address addressDetails = new Address(0L, "456 New St");

        when(addressRepository.findById(0L)).thenReturn(java.util.Optional.of(existingAddress));
        when(addressRepository.save(existingAddress)).thenReturn(addressDetails);

        mockMvc.perform(put("/childAlert/{id}", 0L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("456 New St"));

        verify(addressRepository).save(existingAddress);
    }

    @Test
    public void deleteAddress_SuccessfulDeletion() throws Exception {
        Address existingAddress = new Address(1L, "123 Main St");

        when(addressRepository.findById(1L)).thenReturn(java.util.Optional.of(existingAddress));

        mockMvc.perform(delete("/childAlert/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(addressRepository).delete(existingAddress);
    }
}
