package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.PlatDto;
import itu.m2.ws.models.Plat;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.services.PlatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlatController.class)
@Import(SecurityConfig.class)
public class PlatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PlatService platService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllPlats() throws Exception {
        Plat plat = new Plat();
        plat.setId(1L);
        plat.setNom("Pizza");
        Restaurant r = new Restaurant();
        r.setId(1L);
        plat.setRestaurant(r);

        when(platService.getPlatsByRestaurantId(1L)).thenReturn(Collections.singletonList(plat));

        mockMvc.perform(get("/api/restaurants/1/plats")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Pizza"));
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testCreatePlat() throws Exception {
        PlatDto platDto = new PlatDto();
        platDto.setNom("Burger");
        platDto.setPrix(12.5);

        Plat plat = new Plat();
        plat.setId(1L);
        plat.setNom("Burger");

        when(platService.createPlat(any(Plat.class))).thenReturn(plat);

        mockMvc.perform(post("/api/restaurants/1/plats")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(platDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Burger"));
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testUpdatePlat() throws Exception {
        PlatDto platDto = new PlatDto();
        platDto.setNom("Burger V2");
        platDto.setPrix(13.0);

        Plat plat = new Plat();
        plat.setId(1L);
        plat.setNom("Burger V2");

        when(platService.getPlatByIdAndRestaurantId(1L, 1L)).thenReturn(Optional.of(new Plat()));
        when(platService.updatePlat(any(Long.class), any(Plat.class))).thenReturn(Optional.of(plat));

        mockMvc.perform(put("/api/restaurants/1/plats/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(platDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Burger V2"));
    }

    @Test
    @WithMockUser(roles = "RESTAURANT")
    public void testDeletePlat() throws Exception {
        when(platService.getPlatByIdAndRestaurantId(1L, 1L)).thenReturn(Optional.of(new Plat()));
        when(platService.deletePlat(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/restaurants/1/plats/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
