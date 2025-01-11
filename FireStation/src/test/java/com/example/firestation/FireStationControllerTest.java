package com.example.firestation;

import com.example.firestation.controller.PersonController;
import com.example.firestation.model.FireStation;
import com.example.firestation.repository.FireStationRepository;
import com.example.firestation.service.FireStationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// This tests sending valid data using Mockito. This test is successful, so the test works.
@WebMvcTest
public class FireStationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationRepository fireStationRepository;

    @MockBean
    private FireStationService fireStationService;

    @MockBean
    private PersonController personController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addFireStationTest() throws Exception {
        FireStation fireStation = new FireStation(1, 4, "123 Main St");
        String fireStationJson = objectMapper.writeValueAsString(fireStation);

        mockMvc.perform(post("/firestation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(fireStationJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getPeopleByStationTest() throws Exception {
        int stationNumber = 4;
        Map<String, Object> response = new HashMap<>();
        response.put("JohnDoe", "123 Main St");
        response.put("JaneDoe", "456 Elm St");

        when(fireStationService.getPeopleByStation(stationNumber)).thenReturn(response);

        mockMvc.perform(get("/firestation")
                        .param("stationNumber", String.valueOf(stationNumber))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.JohnDoe").value("123 Main St"))
                .andExpect(jsonPath("$.JaneDoe").value("456 Elm St"));
    }

    @Test
    public void updateFireStation_SuccessfulUpdate() throws Exception {
        long id = 1L;
        FireStation existingFireStation = new FireStation(id, 3, "123 Main St");
        FireStation fireStationDetails = new FireStation(id, 4, "456 Elm St");
        FireStation updatedFireStation = new FireStation(id, 4, "456 Elm St");

        when(fireStationRepository.findById(id)).thenReturn(Optional.of(existingFireStation));
        when(fireStationRepository.save(existingFireStation)).thenReturn(updatedFireStation);

        mockMvc.perform(put("/firestation/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fireStationDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stationNumber").value(4))
                .andExpect(jsonPath("$.address").value("456 Elm St"));
    }

    @Test
    public void deleteFireStation() throws Exception {
        long id = 1L;
        FireStation fireStation = new FireStation(id, 3, "123 Main St");

        when(fireStationRepository.findById(id)).thenReturn(Optional.of(fireStation));

        mockMvc.perform(delete("/firestation/{id}", id))
                .andExpect(status().isNoContent());

        verify(fireStationRepository, times(1)).delete(fireStation);
    }


}
