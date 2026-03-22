package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.HistoriqueLivraisonDto;
import itu.m2.ws.models.Livraison;
import itu.m2.ws.models.HistoriqueLivraison;
import itu.m2.ws.models.StatutLivraison;
import itu.m2.ws.services.HistoriqueLivraisonService;
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

@WebMvcTest(HistoriqueLivraisonController.class)
public class HistoriqueLivraisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistoriqueLivraisonService historiqueLivraisonService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllHistoriqueLivraisons() throws Exception {
        Livraison livraison = new Livraison();
        livraison.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());

        HistoriqueLivraison hist1 = new HistoriqueLivraison(1L, livraison, now, statut);
        HistoriqueLivraison hist2 = new HistoriqueLivraison(2L, livraison, now, statut);

        when(historiqueLivraisonService.getAllHistoriqueLivraisons()).thenReturn(Arrays.asList(hist1, hist2));

        mockMvc.perform(get("/api/historiquelivraisons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetHistoriqueLivraisonById() throws Exception {
        Livraison livraison = new Livraison();
        livraison.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriqueLivraison hist = new HistoriqueLivraison(1L, livraison, now, statut);

        when(historiqueLivraisonService.getHistoriqueLivraisonById(1L)).thenReturn(Optional.of(hist));

        mockMvc.perform(get("/api/historiquelivraisons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateHistoriqueLivraison() throws Exception {
        Livraison livraison = new Livraison();
        livraison.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriqueLivraison hist = new HistoriqueLivraison(1L, livraison, now, statut);
        HistoriqueLivraisonDto histDto = new HistoriqueLivraisonDto(null, 1L, now, 1L);

        when(historiqueLivraisonService.createHistoriqueLivraison(any(HistoriqueLivraison.class))).thenReturn(hist);

        mockMvc.perform(post("/api/historiquelivraisons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(histDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateHistoriqueLivraison() throws Exception {
        Livraison livraison = new Livraison();
        livraison.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(2L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriqueLivraison hist = new HistoriqueLivraison(1L, livraison, now, statut);
        HistoriqueLivraisonDto histDto = new HistoriqueLivraisonDto(1L, 1L, now, 2L);

        when(historiqueLivraisonService.updateHistoriqueLivraison(any(Long.class), any(HistoriqueLivraison.class))).thenReturn(Optional.of(hist));

        mockMvc.perform(put("/api/historiquelivraisons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(histDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutLivraisonId").value(2));
    }

    @Test
    public void testDeleteHistoriqueLivraison() throws Exception {
        when(historiqueLivraisonService.deleteHistoriqueLivraison(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/historiquelivraisons/1"))
                .andExpect(status().isOk());
    }
}
