package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.RestaurantDto;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.ModePaiement;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.CommandeService;
import itu.m2.ws.services.RestaurantService;
import itu.m2.ws.services.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
@Import(SecurityConfig.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    @MockitoBean
    private CommandeService commandeService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllRestaurants() throws Exception {
        Utilisateur user1 = new Utilisateur(1L, "resto1@test.com", "pass", Role.RESTAURANT, true, null);
        Utilisateur user2 = new Utilisateur(2L, "resto2@test.com", "pass", Role.RESTAURANT, true, null);
        Restaurant restaurant1 = new Restaurant(1L, user1, "Chez Paul", "Bon", "123", "Rue", "Ville", 0.0, 0.0, true, 4.5);
        Restaurant restaurant2 = new Restaurant(2L, user2, "Chez Pierre", "Excellent", "456", "Av", "Ville", 1.1, 1.1, true, 4.8);

        when(restaurantService.getAllRestaurants()).thenReturn(Arrays.asList(restaurant1, restaurant2));

        mockMvc.perform(get("/api/restaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nom").value("Chez Paul"))
                .andExpect(jsonPath("$[1].nom").value("Chez Pierre"));
    }

    @Test
    public void testGetRestaurantById() throws Exception {
        Utilisateur user = new Utilisateur(1L, "resto@test.com", "pass", Role.RESTAURANT, true, null);
        Restaurant restaurant = new Restaurant(1L, user, "Chez Paul", "Bon", "123", "Rue", "Ville", 0.0, 0.0, true, 4.5);

        when(restaurantService.getRestaurantById(1L)).thenReturn(Optional.of(restaurant));

        mockMvc.perform(get("/api/restaurants/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Chez Paul"));
    }

    @Test
    @WithMockUser(username = "resto@test.com", roles = "RESTAURANT")
    public void testGetMyCommandes() throws Exception {
        // Mocked for now in controller
        mockMvc.perform(get("/api/restaurants/me/commandes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testAccepterCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setMontantTotal(50.0);
        commande.setModePaiement(ModePaiement.CARTE);
        
        itu.m2.ws.models.Client client = new itu.m2.ws.models.Client();
        client.setId(1L);
        commande.setClient(client);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        commande.setRestaurant(restaurant);

        StatutCommande statut = new StatutCommande();
        statut.setId(2L);
        commande.setStatutCommande(statut);

        when(commandeService.updateStatutCommande(any(Long.class), any(StatutCommande.class))).thenReturn(Optional.of(commande));

        mockMvc.perform(post("/api/restaurants/me/commandes/1/accepter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutCommandeId").value(2));
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testRefuserCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setMontantTotal(50.0);
        commande.setModePaiement(ModePaiement.CARTE);
        
        itu.m2.ws.models.Client client = new itu.m2.ws.models.Client();
        client.setId(1L);
        commande.setClient(client);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        commande.setRestaurant(restaurant);

        StatutCommande statut = new StatutCommande();
        statut.setId(0L);
        commande.setStatutCommande(statut);

        when(commandeService.updateStatutCommande(any(Long.class), any(StatutCommande.class))).thenReturn(Optional.of(commande));

        mockMvc.perform(post("/api/restaurants/me/commandes/1/refuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutCommandeId").value(0));
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testEnPreparationCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setMontantTotal(50.0);
        commande.setModePaiement(ModePaiement.CARTE);
        
        itu.m2.ws.models.Client client = new itu.m2.ws.models.Client();
        client.setId(1L);
        commande.setClient(client);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        commande.setRestaurant(restaurant);

        StatutCommande statut = new StatutCommande();
        statut.setId(3L);
        commande.setStatutCommande(statut);

        when(commandeService.updateStatutCommande(any(Long.class), any(StatutCommande.class))).thenReturn(Optional.of(commande));

        mockMvc.perform(post("/api/restaurants/me/commandes/1/en-preparation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutCommandeId").value(3));
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testPreteeCommande() throws Exception {
        Commande commande = new Commande();
        commande.setId(1L);
        commande.setMontantTotal(50.0);
        commande.setModePaiement(ModePaiement.CARTE);
        
        itu.m2.ws.models.Client client = new itu.m2.ws.models.Client();
        client.setId(1L);
        commande.setClient(client);

        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        commande.setRestaurant(restaurant);

        StatutCommande statut = new StatutCommande();
        statut.setId(4L);
        commande.setStatutCommande(statut);

        when(commandeService.updateStatutCommande(any(Long.class), any(StatutCommande.class))).thenReturn(Optional.of(commande));

        mockMvc.perform(post("/api/restaurants/me/commandes/1/pretee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statutCommandeId").value(4));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testCreateRestaurant() throws Exception {
        Utilisateur user = new Utilisateur(1L, "resto@test.com", "pass", Role.RESTAURANT, true, null);
        Restaurant restaurant = new Restaurant(1L, user, "Chez Paul", "Bon", "123", "Rue", "Ville", 0.0, 0.0, true, 4.5);
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setNom("Chez Paul");
        restaurantDto.setDescription("Bon");
        restaurantDto.setTelephone("123");
        restaurantDto.setAdresse("Rue");
        restaurantDto.setVille("Ville");
        restaurantDto.setLatitude(0.0);
        restaurantDto.setLongitude(0.0);
        restaurantDto.setOuvert(true);
        restaurantDto.setNoteMoyenne(4.5);
        restaurantDto.setEmail("resto@test.com");
        restaurantDto.setMotDePasse("pass");

        when(restaurantService.createRestaurant(any(Restaurant.class))).thenReturn(restaurant);

        mockMvc.perform(post("/api/restaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Chez Paul"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testUpdateRestaurant() throws Exception {
        Utilisateur user = new Utilisateur(1L, "resto@test.com", "pass", Role.RESTAURANT, true, null);
        Restaurant restaurant = new Restaurant(1L, user, "Chez Paul", "Très bon", "123", "Rue", "Ville", 0.0, 0.0, true, 4.5);
        RestaurantDto restaurantDto = new RestaurantDto();
        restaurantDto.setNom("Chez Paul");
        restaurantDto.setDescription("Très bon");
        restaurantDto.setTelephone("123");
        restaurantDto.setAdresse("Rue");
        restaurantDto.setVille("Ville");
        restaurantDto.setLatitude(0.0);
        restaurantDto.setLongitude(0.0);
        restaurantDto.setOuvert(true);
        restaurantDto.setNoteMoyenne(4.5);
        restaurantDto.setEmail("resto@test.com");

        when(restaurantService.getRestaurantById(1L)).thenReturn(Optional.of(restaurant));
        when(restaurantService.updateRestaurant(any(Long.class), any(Restaurant.class))).thenReturn(Optional.of(restaurant));

        mockMvc.perform(put("/api/restaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(restaurantDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Très bon"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteRestaurant() throws Exception {
        when(restaurantService.deleteRestaurant(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isOk());
    }
}
