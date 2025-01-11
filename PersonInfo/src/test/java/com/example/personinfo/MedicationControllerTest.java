package com.example.personinfo;

import com.example.personinfo.controller.PersonController;
import com.example.personinfo.model.Medication;
import com.example.personinfo.repository.MedicationRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

// This tests sending valid data using Mockito. This test is successful, so the test works.
@WebMvcTest
public class MedicationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    // Despite saying they're not being used, I do still need these
    @MockBean
    private MedicationRepository medicationRepository;

    @MockBean
    private PersonController personController;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addMedication() throws Exception {
        Medication medication = new Medication(1, "Penicillin", "55mg");
        String medicationJson = objectMapper.writeValueAsString(medication);

        mockMvc.perform(post("/medication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(medicationJson))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getMedication() throws Exception {
        Medication medication1 = new Medication(1L, "Ibuprofen", "200mg");
        Medication medication2 = new Medication(2L, "Paracetamol", "500mg");

        when(medicationRepository.findAll()).thenReturn(Arrays.asList(medication1, medication2));

        mockMvc.perform(get("/medication")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                        [
                            {"id":1,"name":"Ibuprofen","dosage":"200mg"},
                            {"id":2,"name":"Paracetamol","dosage":"500mg"}
                        ]
                        """));
    }

    @Test
    public void updateMedication() throws Exception {
        Medication existingMedication = new Medication(1L, "Ibuprofen", "200mg");

        when(medicationRepository.findById(1L)).thenReturn(Optional.of(existingMedication));
        when(medicationRepository.save(any(Medication.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockMvc.perform(put("/medication/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "Paracetamol",
                                    "dosage": "500mg"
                                }
                                """))
                .andExpect(status().isOk());
    }
}
