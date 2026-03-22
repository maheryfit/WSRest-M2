package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.dto.ClientDto;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.ClientService;
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

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClientService clientService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
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
    public void testDeleteClient() throws Exception {
        when(clientService.deleteClient(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/clients/1"))
                .andExpect(status().isOk());
    }
}
