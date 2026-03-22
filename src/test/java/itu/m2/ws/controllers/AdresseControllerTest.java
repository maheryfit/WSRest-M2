package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.AdresseDto;
import itu.m2.ws.models.Adresse;
import itu.m2.ws.models.Client;
import itu.m2.ws.services.AdresseService;
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

@WebMvcTest(AdresseController.class)
public class AdresseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdresseService adresseService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllAdresses() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Adresse adresse1 = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);
        Adresse adresse2 = new Adresse(2L, client, "Bureau", "456 Av", "Ville", "12345", 1.1, 1.1, false);

        when(adresseService.getAllAdresses()).thenReturn(Arrays.asList(adresse1, adresse2));

        mockMvc.perform(get("/api/adresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].libelle").value("Maison"))
                .andExpect(jsonPath("$[1].libelle").value("Bureau"));
    }

    @Test
    public void testGetAdresseById() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Adresse adresse = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(adresseService.getAdresseById(1L)).thenReturn(Optional.of(adresse));

        mockMvc.perform(get("/api/adresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.libelle").value("Maison"));
    }

    @Test
    public void testCreateAdresse() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Adresse adresse = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);
        AdresseDto adresseDto = new AdresseDto(null, 1L, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(adresseService.createAdresse(any(Adresse.class))).thenReturn(adresse);

        mockMvc.perform(post("/api/adresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adresseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Maison"));
    }

    @Test
    public void testUpdateAdresse() throws Exception {
        Client client = new Client();
        client.setId(1L);
        Adresse adresse = new Adresse(1L, client, "Domicile", "123 Rue", "Ville", "12345", 0.0, 0.0, true);
        AdresseDto adresseDto = new AdresseDto(1L, 1L, "Domicile", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(adresseService.updateAdresse(any(Long.class), any(Adresse.class))).thenReturn(Optional.of(adresse));

        mockMvc.perform(put("/api/adresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adresseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Domicile"));
    }

    @Test
    public void testDeleteAdresse() throws Exception {
        when(adresseService.deleteAdresse(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/adresses/1"))
                .andExpect(status().isOk());
    }
}
