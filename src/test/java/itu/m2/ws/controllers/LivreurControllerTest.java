package itu.m2.ws.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import itu.m2.ws.configs.SecurityConfig;
import itu.m2.ws.dto.LivreurDto;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.enums.StatutLivreur;
import itu.m2.ws.services.LivreurService;
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

@Import(SecurityConfig.class)
@WebMvcTest(LivreurController.class)
public class LivreurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LivreurService livreurService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetAllLivreurs() throws Exception {
        Utilisateur user1 = new Utilisateur(1L, "livreur1@test.com", "pass", Role.LIVREUR, true, null);
        Utilisateur user2 = new Utilisateur(2L, "livreur2@test.com", "pass", Role.LIVREUR, true, null);
        Livreur livreur1 = new Livreur(1L, user1, "Martin", "Paul", "112233", StatutLivreur.DISPONIBLE);
        Livreur livreur2 = new Livreur(2L, user2, "Bernard", "Luc", "445566", StatutLivreur.EN_LIVRAISON);

        when(livreurService.getAllLivreurs()).thenReturn(Arrays.asList(livreur1, livreur2));

        mockMvc.perform(get("/api/livreurs"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nom").value("Martin"))
                .andExpect(jsonPath("$[1].nom").value("Bernard"));
    }

    @Test
    public void testGetLivreurById() throws Exception {
        Utilisateur user = new Utilisateur(1L, "livreur@test.com", "pass", Role.LIVREUR, true, null);
        Livreur livreur = new Livreur(1L, user, "Martin", "Paul", "112233", StatutLivreur.DISPONIBLE);

        when(livreurService.getLivreurById(1L)).thenReturn(Optional.of(livreur));

        mockMvc.perform(get("/api/livreurs/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nom").value("Martin"));
    }

    @Test
    public void testCreateLivreur() throws Exception {
        Utilisateur user = new Utilisateur(1L, "livreur@test.com", "pass", Role.LIVREUR, true, null);
        Livreur livreur = new Livreur(1L, user, "Martin", "Paul", "112233", StatutLivreur.DISPONIBLE);
        LivreurDto livreurDto = new LivreurDto();
        livreurDto.setNom("Martin");
        livreurDto.setPrenom("Paul");
        livreurDto.setTelephone("112233");
        livreurDto.setStatut(StatutLivreur.DISPONIBLE);
        livreurDto.setEmail("livreur@test.com");
        livreurDto.setMotDePasse("pass");

        when(livreurService.createLivreur(any(Livreur.class))).thenReturn(livreur);

        mockMvc.perform(post("/api/livreurs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livreurDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Martin"));
    }

    @Test
    public void testUpdateLivreur() throws Exception {
        Utilisateur user = new Utilisateur(1L, "livreur@test.com", "pass", Role.LIVREUR, true, null);
        Livreur livreur = new Livreur(1L, user, "Martin", "Paul", "112233", StatutLivreur.HORS_LIGNE);
        LivreurDto livreurDto = new LivreurDto();
        livreurDto.setNom("Martin");
        livreurDto.setPrenom("Paul");
        livreurDto.setTelephone("112233");
        livreurDto.setStatut(StatutLivreur.HORS_LIGNE);
        livreurDto.setEmail("livreur@test.com");

        when(livreurService.getLivreurById(1L)).thenReturn(Optional.of(livreur));
        when(livreurService.updateLivreur(any(Long.class), any(Livreur.class))).thenReturn(Optional.of(livreur));

        mockMvc.perform(put("/api/livreurs/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(livreurDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value("HORS_LIGNE"));
    }

    @Test
    public void testDeleteLivreur() throws Exception {
        when(livreurService.deleteLivreur(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/livreurs/1"))
                .andExpect(status().isOk());
    }
}
