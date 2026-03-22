package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.RestaurantDto;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.RestaurantService;
import itu.m2.ws.services.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestaurantController.class)
public class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RestaurantService restaurantService;

    @MockitoBean
    private UtilisateurService utilisateurService;

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
    public void testDeleteRestaurant() throws Exception {
        when(restaurantService.deleteRestaurant(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/restaurants/1"))
                .andExpect(status().isOk());
    }
}
