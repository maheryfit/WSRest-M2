package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.AdresseDto;
import itu.m2.ws.dto.ClientDto;
import itu.m2.ws.models.Adresse;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.AdresseService;
import itu.m2.ws.services.ClientService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
@Import(SecurityConfig.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private AdresseService adresseService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGetAllClients() throws Exception {
        Utilisateur user1 = new Utilisateur(1L, "test1@test.com", "pass", Role.CLIENT, true, null);
        Utilisateur user2 = new Utilisateur(2L, "test2@test.com", "pass", Role.CLIENT, true, null);
        Client client1 = new Client(1L, user1, "Doe", "John", "12345");
        Client client2 = new Client(2L, user2, "Smith", "Jane", "67890");

        when(clientService.getAllClients()).thenReturn(Arrays.asList(client1, client2));

        mockMvc.perform(get("/api/clients"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nom").value("Doe"))
                .andExpect(jsonPath("$[1].nom").value("Smith"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    public void testGetMyProfile() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");

        when(clientService.getClientByEmail("test@test.com")).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/me"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Doe"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    public void testUpdateMyProfile() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");

        ClientDto clientDto = new ClientDto();
        clientDto.setNom("DoeUpdated");
        clientDto.setPrenom("John");
        clientDto.setTelephone("54321");
        clientDto.setEmail("test@test.com");

        Client updatedClient = new Client(1L, user, "DoeUpdated", "John", "54321");

        when(clientService.getClientByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(clientService.updateClient(any(Long.class), any(Client.class))).thenReturn(Optional.of(updatedClient));

        mockMvc.perform(put("/api/clients/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("DoeUpdated"))
                .andExpect(jsonPath("$.telephone").value("54321"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    public void testGetMyAdresses() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");
        Adresse adresse1 = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(clientService.getClientByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(adresseService.getAllAdresses()).thenReturn(List.of(adresse1));

        mockMvc.perform(get("/api/clients/me/adresses"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].libelle").value("Maison"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    public void testCreateMyAdresse() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");
        AdresseDto adresseDto = new AdresseDto(null, 1L, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);
        Adresse adresse = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(clientService.getClientByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(adresseService.createAdresse(any(Adresse.class))).thenReturn(adresse);

        mockMvc.perform(post("/api/clients/me/adresses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adresseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Maison"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    public void testGetMyAdresseById() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");
        Adresse adresse = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(clientService.getClientByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(adresseService.getAdresseById(1L)).thenReturn(Optional.of(adresse));

        mockMvc.perform(get("/api/clients/me/adresses/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.libelle").value("Maison"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    public void testUpdateMyAdresse() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");
        Adresse adresse = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);
        AdresseDto adresseDto = new AdresseDto(1L, 1L, "Bureau", "123 Rue", "Ville", "12345", 0.0, 0.0, true);
        Adresse updatedAdresse = new Adresse(1L, client, "Bureau", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(clientService.getClientByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(adresseService.getAdresseById(1L)).thenReturn(Optional.of(adresse));
        when(adresseService.updateAdresse(any(Long.class), any(Adresse.class))).thenReturn(Optional.of(updatedAdresse));

        mockMvc.perform(put("/api/clients/me/adresses/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adresseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libelle").value("Bureau"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = "CLIENT")
    public void testDeleteMyAdresse() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");
        Adresse adresse = new Adresse(1L, client, "Maison", "123 Rue", "Ville", "12345", 0.0, 0.0, true);

        when(clientService.getClientByEmail("test@test.com")).thenReturn(Optional.of(client));
        when(adresseService.getAdresseById(1L)).thenReturn(Optional.of(adresse));
        when(adresseService.deleteAdresse(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/clients/me/adresses/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    public void testGetClientById() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");

        when(clientService.getClientById(1L)).thenReturn(Optional.of(client));

        mockMvc.perform(get("/api/clients/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Doe"));
    }

    @Test
    @WithMockUser
    public void testCreateClient() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "12345");
        ClientDto clientDto = new ClientDto();
        clientDto.setNom("Doe");
        clientDto.setPrenom("John");
        clientDto.setTelephone("12345");
        clientDto.setEmail("test@test.com");
        clientDto.setMotDePasse("pass");

        when(clientService.createClient(any(Client.class))).thenReturn(client);

        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Doe"));
    }

    @Test
    @WithMockUser(roles = "CLIENT")
    public void testUpdateClient() throws Exception {
        Utilisateur user = new Utilisateur(1L, "test@test.com", "pass", Role.CLIENT, true, null);
        Client client = new Client(1L, user, "Doe", "John", "54321");
        ClientDto clientDto = new ClientDto();
        clientDto.setNom("Doe");
        clientDto.setPrenom("John");
        clientDto.setTelephone("54321");
        clientDto.setEmail("test@test.com");

        when(clientService.getClientById(1L)).thenReturn(Optional.of(client));
        when(clientService.updateClient(any(Long.class), any(Client.class))).thenReturn(Optional.of(client));

        mockMvc.perform(put("/api/clients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.telephone").value("54321"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testDeleteClient() throws Exception {
        when(clientService.deleteClient(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isOk());
    }
}
