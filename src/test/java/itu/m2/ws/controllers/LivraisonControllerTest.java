package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.LivraisonDto;
import itu.m2.ws.models.*;
import itu.m2.ws.services.LivraisonService;
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

@Import(SecurityConfig.class)
@WebMvcTest(LivraisonController.class)
public class LivraisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LivraisonService livraisonService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllLivraisons() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Livreur livreur = new Livreur();
        livreur.setId(1L);
        Adresse adresse = new Adresse();
        adresse.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());

        Livraison livraison1 = new Livraison(1L, commande, livreur, adresse, now, null, now, statut);
        Livraison livraison2 = new Livraison(2L, commande, livreur, adresse, now, null, now, statut);

        when(livraisonService.getAllLivraisons()).thenReturn(Arrays.asList(livraison1, livraison2));

        mockMvc.perform(get("/api/livraisons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    public void testGetLivraisonById() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Livreur livreur = new Livreur();
        livreur.setId(1L);
        Adresse adresse = new Adresse();
        adresse.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        Livraison livraison = new Livraison(1L, commande, livreur, adresse, now, null, now, statut);

        when(livraisonService.getLivraisonById(1L)).thenReturn(Optional.of(livraison));

        mockMvc.perform(get("/api/livraisons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testCreateLivraison() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Livreur livreur = new Livreur();
        livreur.setId(1L);
        Adresse adresse = new Adresse();
        adresse.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        Livraison livraison = new Livraison(1L, commande, livreur, adresse, now, null, now, statut);
        LivraisonDto livraisonDto = new LivraisonDto(null, 1L, 1L, 1L, now, null, 1L);

        when(livraisonService.createLivraison(any(Livraison.class))).thenReturn(livraison);

        mockMvc.perform(post("/api/livraisons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livraisonDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateLivraison() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Livreur livreur = new Livreur();
        livreur.setId(1L);
        Adresse adresse = new Adresse();
        adresse.setId(1L);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(2L);
        Timestamp now = Timestamp.from(Instant.now());
        Livraison livraison = new Livraison(1L, commande, livreur, adresse, now, now, now, statut);
        LivraisonDto livraisonDto = new LivraisonDto(1L, 1L, 1L, 1L, now, now, 2L);

        when(livraisonService.updateLivraison(any(Long.class), any(Livraison.class))).thenReturn(Optional.of(livraison));

        mockMvc.perform(put("/api/livraisons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livraisonDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutLivraisonId").value(2));
    }

    @Test
    public void testDeleteLivraison() throws Exception {
        when(livraisonService.deleteLivraison(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/livraisons/1"))
                .andExpect(status().isOk());
    }
}
