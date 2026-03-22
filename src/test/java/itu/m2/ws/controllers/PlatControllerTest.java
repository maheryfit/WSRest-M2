package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.PlatDto;
import itu.m2.ws.models.Plat;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.services.PlatService;
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

@WebMvcTest(PlatController.class)
public class PlatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlatService platService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllPlats() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);

        Plat plat1 = new Plat(1L, restaurant, "Pizza", "Fromage", 12.5, "Pizza", true);
        Plat plat2 = new Plat(2L, restaurant, "Burger", "Viande", 10.0, "Burger", true);

        when(platService.getAllPlats()).thenReturn(Arrays.asList(plat1, plat2));

        mockMvc.perform(get("/api/plats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nom").value("Pizza"))
                .andExpect(jsonPath("$[1].nom").value("Burger"));
    }

    @Test
    public void testGetPlatById() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Plat plat = new Plat(1L, restaurant, "Pizza", "Fromage", 12.5, "Pizza", true);

        when(platService.getPlatById(1L)).thenReturn(Optional.of(plat));

        mockMvc.perform(get("/api/plats/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Pizza"));
    }

    @Test
    public void testCreatePlat() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Plat plat = new Plat(1L, restaurant, "Pizza", "Fromage", 12.5, "Pizza", true);
        PlatDto platDto = new PlatDto(null, 1L, "Pizza", "Fromage", 12.5, "Pizza", true);

        when(platService.createPlat(any(Plat.class))).thenReturn(plat);

        mockMvc.perform(post("/api/plats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(platDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Pizza"));
    }

    @Test
    public void testUpdatePlat() throws Exception {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(1L);
        Plat plat = new Plat(1L, restaurant, "Pizza", "Fromage", 15.0, "Pizza", true);
        PlatDto platDto = new PlatDto(1L, 1L, "Pizza", "Fromage", 15.0, "Pizza", true);

        when(platService.updatePlat(any(Long.class), any(Plat.class))).thenReturn(Optional.of(plat));

        mockMvc.perform(put("/api/plats/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(platDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prix").value(15.0));
    }

    @Test
    public void testDeletePlat() throws Exception {
        when(platService.deletePlat(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/plats/1"))
                .andExpect(status().isOk());
    }
}
