package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.StatutCommandeDto;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.services.StatutCommandeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(StatutCommandeController.class)
public class StatutCommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatutCommandeService statutCommandeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllStatutCommandes() throws Exception {
        StatutCommande statut1 = new StatutCommande(1L, "Créée", 1);
        StatutCommande statut2 = new StatutCommande(2L, "En préparation", 2);

        when(statutCommandeService.getAllStatutCommandes()).thenReturn(Arrays.asList(statut1, statut2));

        mockMvc.perform(get("/api/statutcommandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].libelle").value("Créée"))
                .andExpect(jsonPath("$[1].libelle").value("En préparation"));
    }

    @Test
    public void testGetStatutCommandeById() throws Exception {
        StatutCommande statut = new StatutCommande(1L, "Créée", 1);

        when(statutCommandeService.getStatutCommandeById(1L)).thenReturn(Optional.of(statut));

        mockMvc.perform(get("/api/statutcommandes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.libelle").value("Créée"));
    }

    @Test
    public void testCreateStatutCommande() throws Exception {
        StatutCommande statut = new StatutCommande(1L, "Créée", 1);
        StatutCommandeDto statutDto = new StatutCommandeDto(null, "Créée", 1);

        when(statutCommandeService.createStatutCommande(any(StatutCommande.class))).thenReturn(statut);

        mockMvc.perform(post("/api/statutcommandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Créée"));
    }

    @Test
    public void testUpdateStatutCommande() throws Exception {
        StatutCommande statut = new StatutCommande(1L, "Acceptée", 2);
        StatutCommandeDto statutDto = new StatutCommandeDto(1L, "Acceptée", 2);

        when(statutCommandeService.updateStatutCommande(any(Long.class), any(StatutCommande.class))).thenReturn(Optional.of(statut));

        mockMvc.perform(put("/api/statutcommandes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Acceptée"));
    }

    @Test
    public void testDeleteStatutCommande() throws Exception {
        when(statutCommandeService.deleteStatutCommande(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/statutcommandes/1"))
                .andExpect(status().isOk());
    }
}
