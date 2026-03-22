package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.AvisRestaurantDto;
import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.services.AvisRestaurantService;
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

@WebMvcTest(AvisRestaurantController.class)
public class AvisRestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AvisRestaurantService avisRestaurantService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllAvisRestaurants() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Client client = new Client();
        client.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());

        AvisRestaurant avis1 = new AvisRestaurant(1L, restaurant, client, 4, "Bon", now);
        AvisRestaurant avis2 = new AvisRestaurant(2L, restaurant, client, 5, "Excellent", now);

        when(avisRestaurantService.getAllAvisRestaurants()).thenReturn(Arrays.asList(avis1, avis2));

        mockMvc.perform(get("/api/avisrestaurants"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].note").value(4))
                .andExpect(jsonPath("$[1].note").value(5));
    }

    @Test
    public void testGetAvisRestaurantById() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Client client = new Client();
        client.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        AvisRestaurant avis = new AvisRestaurant(1L, restaurant, client, 4, "Bon", now);

        when(avisRestaurantService.getAvisRestaurantById(1L)).thenReturn(Optional.of(avis));

        mockMvc.perform(get("/api/avisrestaurants/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.note").value(4));
    }

    @Test
    public void testCreateAvisRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Client client = new Client();
        client.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        AvisRestaurant avis = new AvisRestaurant(1L, restaurant, client, 4, "Bon", now);
        AvisRestaurantDto avisDto = new AvisRestaurantDto(null, 1L, 1L, 4, "Bon", null);

        when(avisRestaurantService.createAvisRestaurant(any(AvisRestaurant.class))).thenReturn(avis);

        mockMvc.perform(post("/api/avisrestaurants")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avisDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note").value(4));
    }

    @Test
    public void testUpdateAvisRestaurant() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Client client = new Client();
        client.setId(1L);
        Timestamp now = Timestamp.from(Instant.now());
        AvisRestaurant avis = new AvisRestaurant(1L, restaurant, client, 5, "Très bon", now);
        AvisRestaurantDto avisDto = new AvisRestaurantDto(1L, 1L, 1L, 5, "Très bon", now);

        when(avisRestaurantService.updateAvisRestaurant(any(Long.class), any(AvisRestaurant.class))).thenReturn(Optional.of(avis));

        mockMvc.perform(put("/api/avisrestaurants/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(avisDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.note").value(5));
    }

    @Test
    public void testDeleteAvisRestaurant() throws Exception {
        when(avisRestaurantService.deleteAvisRestaurant(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/avisrestaurants/1"))
                .andExpect(status().isOk());
    }
}
