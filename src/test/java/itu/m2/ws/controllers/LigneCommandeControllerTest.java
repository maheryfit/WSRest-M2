package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.LigneCommandeDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.LigneCommande;
import itu.m2.ws.models.Plat;
import itu.m2.ws.services.LigneCommandeService;
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

@WebMvcTest(LigneCommandeController.class)
@Import(SecurityConfig.class)
public class LigneCommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LigneCommandeService ligneCommandeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllLigneCommandes() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Plat plat = new Plat();
        plat.setId(1L);

        LigneCommande ligne1 = new LigneCommande(1L, commande, plat, 2, 12.5);
        LigneCommande ligne2 = new LigneCommande(2L, commande, plat, 1, 10.0);

        when(ligneCommandeService.getAllLigneCommandes()).thenReturn(Arrays.asList(ligne1, ligne2));

        mockMvc.perform(get("/api/lignecommandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].quantite").value(2))
                .andExpect(jsonPath("$[1].quantite").value(1));
    }

    @Test
    public void testGetLigneCommandeById() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Plat plat = new Plat();
        plat.setId(1L);
        LigneCommande ligne = new LigneCommande(1L, commande, plat, 2, 12.5);

        when(ligneCommandeService.getLigneCommandeById(1L)).thenReturn(Optional.of(ligne));

        mockMvc.perform(get("/api/lignecommandes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.quantite").value(2));
    }

    @Test
    public void testCreateLigneCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Plat plat = new Plat();
        plat.setId(1L);
        LigneCommande ligne = new LigneCommande(1L, commande, plat, 2, 12.5);
        LigneCommandeDto ligneDto = new LigneCommandeDto(null, 1L, 1L, 2, 12.5);

        when(ligneCommandeService.createLigneCommande(any(LigneCommande.class))).thenReturn(ligne);

        mockMvc.perform(post("/api/lignecommandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ligneDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite").value(2));
    }

    @Test
    public void testUpdateLigneCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        Plat plat = new Plat();
        plat.setId(1L);
        LigneCommande ligne = new LigneCommande(1L, commande, plat, 3, 12.5);
        LigneCommandeDto ligneDto = new LigneCommandeDto(1L, 1L, 1L, 3, 12.5);

        when(ligneCommandeService.updateLigneCommande(any(Long.class), any(LigneCommande.class))).thenReturn(Optional.of(ligne));

        mockMvc.perform(put("/api/lignecommandes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ligneDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantite").value(3));
    }

    @Test
    public void testDeleteLigneCommande() throws Exception {
        when(ligneCommandeService.deleteLigneCommande(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/lignecommandes/1"))
                .andExpect(status().isOk());
    }
}
