package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.HistoriquePaiementDto;
import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.HistoriquePaiement;
import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.services.HistoriquePaiementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
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

@WebMvcTest(HistoriquePaiementController.class)
@Import(SecurityConfig.class)
public class HistoriquePaiementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistoriquePaiementService historiquePaiementService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllHistoriquePaiements() throws Exception {
        Paiement paiement = new Paiement();
        paiement.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());

        HistoriquePaiement hist1 = new HistoriquePaiement(1L, paiement, now, statut);
        HistoriquePaiement hist2 = new HistoriquePaiement(2L, paiement, now, statut);

        when(historiquePaiementService.getAllHistoriquePaiements()).thenReturn(Arrays.asList(hist1, hist2));

        mockMvc.perform(get("/api/historiquepaiements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetHistoriquePaiementById() throws Exception {
        Paiement paiement = new Paiement();
        paiement.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriquePaiement hist = new HistoriquePaiement(1L, paiement, now, statut);

        when(historiquePaiementService.getHistoriquePaiementById(1L)).thenReturn(Optional.of(hist));

        mockMvc.perform(get("/api/historiquepaiements/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateHistoriquePaiement() throws Exception {
        Paiement paiement = new Paiement();
        paiement.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriquePaiement hist = new HistoriquePaiement(1L, paiement, now, statut);
        HistoriquePaiementDto histDto = new HistoriquePaiementDto(null, 1L, now, 1L);

        when(historiquePaiementService.createHistoriquePaiement(any(HistoriquePaiement.class))).thenReturn(hist);

        mockMvc.perform(post("/api/historiquepaiements")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(histDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateHistoriquePaiement() throws Exception {
        Paiement paiement = new Paiement();
        paiement.setId(1L);
        StatutPaiement statut = new StatutPaiement();
        statut.setId(2L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriquePaiement hist = new HistoriquePaiement(1L, paiement, now, statut);
        HistoriquePaiementDto histDto = new HistoriquePaiementDto(1L, 1L, now, 2L);

        when(historiquePaiementService.updateHistoriquePaiement(any(Long.class), any(HistoriquePaiement.class))).thenReturn(Optional.of(hist));

        mockMvc.perform(put("/api/historiquepaiements/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(histDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutPaiementId").value(2));
    }

    @Test
    public void testDeleteHistoriquePaiement() throws Exception {
        when(historiquePaiementService.deleteHistoriquePaiement(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/historiquepaiements/1"))
                .andExpect(status().isOk());
    }
}
