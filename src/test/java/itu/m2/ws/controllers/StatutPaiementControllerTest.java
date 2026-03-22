package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.StatutPaiementDto;
import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.services.StatutPaiementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatutPaiementController.class)
public class StatutPaiementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatutPaiementService statutPaiementService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllStatutPaiements() throws Exception {
        StatutPaiement statut1 = new StatutPaiement(1L, "En attente", 1);
        StatutPaiement statut2 = new StatutPaiement(2L, "Succès", 2);

        when(statutPaiementService.getAllStatutPaiements()).thenReturn(Arrays.asList(statut1, statut2));

        mockMvc.perform(get("/api/statutpaiements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].libelle").value("En attente"))
                .andExpect(jsonPath("$[1].libelle").value("Succès"));
    }

    @Test
    public void testGetStatutPaiementById() throws Exception {
        StatutPaiement statut = new StatutPaiement(1L, "En attente", 1);

        when(statutPaiementService.getStatutPaiementById(1L)).thenReturn(Optional.of(statut));

        mockMvc.perform(get("/api/statutpaiements/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.libelle").value("En attente"));
    }

    @Test
    public void testCreateStatutPaiement() throws Exception {
        StatutPaiement statut = new StatutPaiement(1L, "En attente", 1);
        StatutPaiementDto statutDto = new StatutPaiementDto(null, "En attente", 1);

        when(statutPaiementService.createStatutPaiement(any(StatutPaiement.class))).thenReturn(statut);

        mockMvc.perform(post("/api/statutpaiements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("En attente"));
    }

    @Test
    public void testUpdateStatutPaiement() throws Exception {
        StatutPaiement statut = new StatutPaiement(1L, "Échec", 0);
        StatutPaiementDto statutDto = new StatutPaiementDto(1L, "Échec", 0);

        when(statutPaiementService.updateStatutPaiement(any(Long.class), any(StatutPaiement.class))).thenReturn(Optional.of(statut));

        mockMvc.perform(put("/api/statutpaiements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Échec"));
    }

    @Test
    public void testDeleteStatutPaiement() throws Exception {
        when(statutPaiementService.deleteStatutPaiement(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/statutpaiements/1"))
                .andExpect(status().isOk());
    }
}
