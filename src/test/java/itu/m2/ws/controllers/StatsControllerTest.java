package itu.m2.ws.controllers;

import itu.m2.ws.dto.CommandesParJourDto;
import itu.m2.ws.dto.MeilleurClientDto;
import itu.m2.ws.dto.PerformanceLivreurDto;
import itu.m2.ws.dto.TopRestaurantDto;
import itu.m2.ws.repositories.ClientRepository;
import itu.m2.ws.repositories.CommandeRepository;
import itu.m2.ws.repositories.LivreurRepository;
import itu.m2.ws.repositories.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantRepository restaurantRepository;

    @MockitoBean
    private ClientRepository clientRepository;

    @MockitoBean
    private LivreurRepository livreurRepository;

    @MockitoBean
    private CommandeRepository commandeRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetTopRestaurants() throws Exception {
        TopRestaurantDto dto = new TopRestaurantDto(1L, "Le Grand Restaurant", 100L, 4.5, 50000.0);
        when(restaurantRepository.findTopRestaurants(any(Timestamp.class), any(Timestamp.class)))
                .thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/stats/restaurants/top?from=2023-01-01&to=2023-12-31")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Le Grand Restaurant"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetMeilleursClients() throws Exception {
        MeilleurClientDto dto = new MeilleurClientDto(1L, "Jean Dupont", 1200.50, 15L, 80.03);
        when(clientRepository.findMeilleursClients()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/stats/clients/meilleurs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Jean Dupont"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetLivreursPerformance() throws Exception {
        PerformanceLivreurDto dto = new PerformanceLivreurDto(1L, "Paul Martin", 200L, 25.5, 5.0);
        when(livreurRepository.getLivreursPerformance()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/stats/livreurs/performance")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Paul Martin"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetCommandesParJour() throws Exception {
        CommandesParJourDto dto = new CommandesParJourDto(new Date(System.currentTimeMillis()), 50L);
        when(commandeRepository.countCommandesByJour()).thenReturn(Collections.singletonList(dto));

        mockMvc.perform(get("/api/stats/commandes/par-jour")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreCommandes").value(50));
    }
}
