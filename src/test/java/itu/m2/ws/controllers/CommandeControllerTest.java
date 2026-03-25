package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.CommandeDto;
import itu.m2.ws.models.*;
import itu.m2.ws.enums.ModePaiement;
import itu.m2.ws.services.CommandeService;
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

@WebMvcTest(CommandeController.class)
@Import(SecurityConfig.class)
public class CommandeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CommandeService commandeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllCommandes() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());

        Commande commande1 = new Commande(1L, client, restaurant, statut, 50.0, ModePaiement.CARTE, now);
        Commande commande2 = new Commande(2L, client, restaurant, statut, 75.0, ModePaiement.CASH, now);

        when(commandeService.getAllCommandes()).thenReturn(Arrays.asList(commande1, commande2));

        mockMvc.perform(get("/api/commandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].montantTotal").value(50.0))
                .andExpect(jsonPath("$[1].montantTotal").value(75.0));
    }

    @Test
    public void testGetCommandeById() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        Commande commande = new Commande(1L, client, restaurant, statut, 50.0, ModePaiement.CARTE, now);

        when(commandeService.getCommandeById(1L)).thenReturn(Optional.of(commande));

        mockMvc.perform(get("/api/commandes/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.montantTotal").value(50.0));
    }

    @Test
    public void testCreateCommande() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        Commande commande = new Commande(1L, client, restaurant, statut, 50.0, ModePaiement.CARTE, now);
        CommandeDto commandeDto = new CommandeDto(null, 1L, 1L, 1L, 50.0, ModePaiement.CARTE, null);

        when(commandeService.createCommande(any(Commande.class))).thenReturn(commande);

        mockMvc.perform(post("/api/commandes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commandeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montantTotal").value(50.0));
    }

    @Test
    public void testUpdateCommande() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        StatutCommande statut = new StatutCommande();
        statut.setId(2L);
        Timestamp now = Timestamp.from(Instant.now());
        Commande commande = new Commande(1L, client, restaurant, statut, 55.0, ModePaiement.CARTE, now);
        CommandeDto commandeDto = new CommandeDto(1L, 1L, 1L, 2L, 55.0, ModePaiement.CARTE, now);

        when(commandeService.updateCommande(any(Long.class), any(Commande.class))).thenReturn(Optional.of(commande));

        mockMvc.perform(put("/api/commandes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commandeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.montantTotal").value(55.0));
    }

    @Test
    public void testDeleteCommande() throws Exception {
        when(commandeService.deleteCommande(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/commandes/1"))
                .andExpect(status().isOk());
    }
}
