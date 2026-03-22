package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.LoginRequestDto;
import itu.m2.ws.dto.UtilisateurDto;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.UtilisateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UtilisateurController.class)
@Import(SecurityConfig.class) // <- IMPORTANT : Charge notre configuration de sécurité (qui désactive CSRF et permet /login)
public class UtilisateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UtilisateurService utilisateurService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testLogin() throws Exception {
        Utilisateur user = new Utilisateur(1L, "user@test.com", "pass", Role.CLIENT, true, null);
        LoginRequestDto loginRequest = new LoginRequestDto("user@test.com", "pass");
        String fakeToken = "fake-jwt-token";

        when(utilisateurService.logIn(loginRequest.getEmail(), loginRequest.getMotDePasse())).thenReturn(user);
        when(utilisateurService.generateToken(user.getEmail())).thenReturn(fakeToken);

        mockMvc.perform(post("/api/utilisateurs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(fakeToken));
    }

    @Test
    public void testLoginFailure() throws Exception {
        LoginRequestDto loginRequest = new LoginRequestDto("user@test.com", "wrongpass");

        when(utilisateurService.logIn(loginRequest.getEmail(), loginRequest.getMotDePasse()))
                .thenThrow(new RuntimeException("Invalid email or password"));

        mockMvc.perform(post("/api/utilisateurs/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid email or password"));
    }

    @Test
    public void testGetAllUtilisateurs() throws Exception {
        Utilisateur user1 = new Utilisateur(1L, "user1@test.com", "pass", Role.CLIENT, true, null);
        Utilisateur user2 = new Utilisateur(2L, "user2@test.com", "pass", Role.ADMIN, true, null);

        when(utilisateurService.getAllUtilisateurs()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/api/utilisateurs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email").value("user1@test.com"))
                .andExpect(jsonPath("$[1].email").value("user2@test.com"));
    }

    @Test
    public void testGetUtilisateurById() throws Exception {
        Utilisateur user = new Utilisateur(1L, "user@test.com", "pass", Role.CLIENT, true, null);

        when(utilisateurService.getUtilisateurById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/utilisateurs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @Test
    public void testCreateUtilisateur() throws Exception {
        Utilisateur user = new Utilisateur(1L, "user@test.com", "pass", Role.CLIENT, true, null);
        UtilisateurDto userDto = new UtilisateurDto(null, "user@test.com", "pass", Role.CLIENT, true, null);

        when(utilisateurService.createUtilisateur(any(Utilisateur.class))).thenReturn(user);

        mockMvc.perform(post("/api/utilisateurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("user@test.com"));
    }

    @Test
    public void testUpdateUtilisateur() throws Exception {
        Utilisateur user = new Utilisateur(1L, "user@test.com", "newpass", Role.CLIENT, false, null);
        UtilisateurDto userDto = new UtilisateurDto(1L, "user@test.com", "newpass", Role.CLIENT, false, null);

        when(utilisateurService.updateUtilisateur(any(Long.class), any(Utilisateur.class))).thenReturn(Optional.of(user));

        mockMvc.perform(put("/api/utilisateurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.actif").value(false));
    }

    @Test
    public void testDeleteUtilisateur() throws Exception {
        when(utilisateurService.deleteUtilisateur(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/utilisateurs/1"))
                .andExpect(status().isOk());
    }
}
