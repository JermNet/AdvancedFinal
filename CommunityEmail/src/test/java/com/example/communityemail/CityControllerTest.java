package com.example.communityemail;

import com.example.communityemail.controller.PersonController;
import com.example.communityemail.model.City;
import com.example.communityemail.repository.CityRepository;
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

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// This tests sending valid data using Mockito. This test is successful, so the test works.
@WebMvcTest
public class CityControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityRepository cityRepository;

    @MockBean
    private PersonController personController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addCity() throws Exception {
        City city = new City(1, 4, "City", "B#A-123");
        String cityJson = objectMapper.writeValueAsString(city);

        mockMvc.perform(post("/city")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(cityJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getAllCities() throws Exception {
        List<City> cities = List.of(
                new City(1L, 1000, "New York", "333-3333"),
                new City(2L, 2000, "Tokyo", "444-4444")
        );
        Mockito.when(cityRepository.findAll()).thenReturn(cities);

        mockMvc.perform(get("/city"))
                .andExpect(status().isOk());
    }

    @Test
    void updateCity() throws Exception {
        City existingCity = new City(1L, 1000, "New York", "333-3333");
        City updatedCityDetails = new City(2L, 2000, "Tokyo", "444-4444");

        Mockito.when(cityRepository.findById(1L)).thenReturn(Optional.of(existingCity));
        Mockito.when(cityRepository.save(Mockito.any(City.class))).thenReturn(updatedCityDetails);

        mockMvc.perform(put("/city/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "population": 2000,
                                    "name": "Tokyo",
                                    "areaCode": "444-4444"
                                }
                                """))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCity() throws Exception {
        City existingCity = new City(1L, 1000, "New York", "333-3333");

        Mockito.when(cityRepository.findById(1L)).thenReturn(Optional.of(existingCity));

        mockMvc.perform(delete("/city/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
