package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.PaiementDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.services.PaiementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaiementController.class)
public class PaiementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaiementService paiementService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllPaiements() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());

        Paiement paiement1 = new Paiement(1L, commande, 50.0, statut, now);
        Paiement paiement2 = new Paiement(2L, commande, 75.0, statut, now);

        when(paiementService.getAllPaiements()).thenReturn(Arrays.asList(paiement1, paiement2));

        mockMvc.perform(get("/api/paiements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].montant").value(50.0))
                .andExpect(jsonPath("$[1].montant").value(75.0));
    }

    @Test
    public void testGetPaiementById() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        Paiement paiement = new Paiement(1L, commande, 50.0, statut, now);

        when(paiementService.getPaiementById(1L)).thenReturn(Optional.of(paiement));

        mockMvc.perform(get("/api/paiements/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.montant").value(50.0));
    }

    @Test
    public void testCreatePaiement() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        Paiement paiement = new Paiement(1L, commande, 50.0, statut, now);
        PaiementDto paiementDto = new PaiementDto(null, 1L, 50.0, 1L, null);

        when(paiementService.createPaiement(any(Paiement.class))).thenReturn(paiement);

        mockMvc.perform(post("/api/paiements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paiementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montant").value(50.0));
    }

    @Test
    public void testUpdatePaiement() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(2L);
        Timestamp now = Timestamp.from(Instant.now());
        Paiement paiement = new Paiement(1L, commande, 55.0, statut, now);
        PaiementDto paiementDto = new PaiementDto(1L, 1L, 55.0, 2L, now);

        when(paiementService.updatePaiement(any(Long.class), any(Paiement.class))).thenReturn(Optional.of(paiement));

        mockMvc.perform(put("/api/paiements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(paiementDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montant").value(55.0));
    }

    @Test
    public void testDeletePaiement() throws Exception {
        when(paiementService.deletePaiement(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/paiements/1"))
                .andExpect(status().isOk());
    }
}
