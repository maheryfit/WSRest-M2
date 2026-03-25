package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.HistoriqueCommandeDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.HistoriqueCommande;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.services.HistoriqueCommandeService;
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

@WebMvcTest(HistoriqueCommandeController.class)
@Import(SecurityConfig.class)
public class HistoriqueCommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private HistoriqueCommandeService historiqueCommandeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllHistoriqueCommandes() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());

        HistoriqueCommande hist1 = new HistoriqueCommande(1L, commande, now, statut);
        HistoriqueCommande hist2 = new HistoriqueCommande(2L, commande, now, statut);

        when(historiqueCommandeService.getAllHistoriqueCommandes()).thenReturn(Arrays.asList(hist1, hist2));

        mockMvc.perform(get("/api/historiquecommandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetHistoriqueCommandeById() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriqueCommande hist = new HistoriqueCommande(1L, commande, now, statut);

        when(historiqueCommandeService.getHistoriqueCommandeById(1L)).thenReturn(Optional.of(hist));

        mockMvc.perform(get("/api/historiquecommandes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateHistoriqueCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriqueCommande hist = new HistoriqueCommande(1L, commande, now, statut);
        HistoriqueCommandeDto histDto = new HistoriqueCommandeDto(null, 1L, now, 1L);

        when(historiqueCommandeService.createHistoriqueCommande(any(HistoriqueCommande.class))).thenReturn(hist);

        mockMvc.perform(post("/api/historiquecommandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(histDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateHistoriqueCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(2L);
        Timestamp now = Timestamp.from(Instant.now());
        HistoriqueCommande hist = new HistoriqueCommande(1L, commande, now, statut);
        HistoriqueCommandeDto histDto = new HistoriqueCommandeDto(1L, 1L, now, 2L);

        when(historiqueCommandeService.updateHistoriqueCommande(any(Long.class), any(HistoriqueCommande.class))).thenReturn(Optional.of(hist));

        mockMvc.perform(put("/api/historiquecommandes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(histDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutCommandeId").value(2));
    }

    @Test
    public void testDeleteHistoriqueCommande() throws Exception {
        when(historiqueCommandeService.deleteHistoriqueCommande(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/historiquecommandes/1"))
                .andExpect(status().isOk());
    }
}
