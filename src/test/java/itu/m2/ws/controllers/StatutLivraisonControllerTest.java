package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.StatutLivraisonDto;
import itu.m2.ws.models.StatutLivraison;
import itu.m2.ws.services.StatutLivraisonService;
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
@WebMvcTest(StatutLivraisonController.class)
public class StatutLivraisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StatutLivraisonService statutLivraisonService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllStatutLivraisons() throws Exception {
        StatutLivraison statut1 = new StatutLivraison(1L, "Acceptée", 1);
        StatutLivraison statut2 = new StatutLivraison(2L, "En livraison", 2);

        when(statutLivraisonService.getAllStatutLivraisons()).thenReturn(Arrays.asList(statut1, statut2));

        mockMvc.perform(get("/api/statutlivraisons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].libelle").value("Acceptée"))
                .andExpect(jsonPath("$[1].libelle").value("En livraison"));
    }

    @Test
    public void testGetStatutLivraisonById() throws Exception {
        StatutLivraison statut = new StatutLivraison(1L, "Acceptée", 1);

        when(statutLivraisonService.getStatutLivraisonById(1L)).thenReturn(Optional.of(statut));

        mockMvc.perform(get("/api/statutlivraisons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.libelle").value("Acceptée"));
    }

    @Test
    public void testCreateStatutLivraison() throws Exception {
        StatutLivraison statut = new StatutLivraison(1L, "Acceptée", 1);
        StatutLivraisonDto statutDto = new StatutLivraisonDto(null, "Acceptée", 1);

        when(statutLivraisonService.createStatutLivraison(any(StatutLivraison.class))).thenReturn(statut);

        mockMvc.perform(post("/api/statutlivraisons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Acceptée"));
    }

    @Test
    public void testUpdateStatutLivraison() throws Exception {
        StatutLivraison statut = new StatutLivraison(1L, "Livrée", 3);
        StatutLivraisonDto statutDto = new StatutLivraisonDto(1L, "Livrée", 3);

        when(statutLivraisonService.updateStatutLivraison(any(Long.class), any(StatutLivraison.class))).thenReturn(Optional.of(statut));

        mockMvc.perform(put("/api/statutlivraisons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statutDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Livrée"));
    }

    @Test
    public void testDeleteStatutLivraison() throws Exception {
        when(statutLivraisonService.deleteStatutLivraison(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/statutlivraisons/1"))
                .andExpect(status().isOk());
    }
}
