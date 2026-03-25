package itu.m2.ws.controllers;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import itu.m2.ws.enums.Role;
import itu.m2.ws.enums.StatutLivreur;
import itu.m2.ws.models.Commande;
import itu.m2.ws.models.Livraison;
import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.StatutLivraison;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.CommandeService;
import itu.m2.ws.services.LivraisonService;
import itu.m2.ws.services.LivreurService;
import itu.m2.ws.services.UtilisateurService;

@WebMvcTest(LivreurController.class)
public class LivreurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LivreurService livreurService;

    @MockitoBean
    private LivraisonService livraisonService;

    @MockitoBean
    private CommandeService commandeService;

    @MockitoBean
    private UtilisateurService utilisateurService;

    private Livreur livreur;
    private Utilisateur utilisateur;

    @BeforeEach
    void setUp() {
        utilisateur = new Utilisateur();
        utilisateur.setId(1L);
        utilisateur.setEmail("livreur@test.com");
        utilisateur.setRole(Role.LIVREUR);

        livreur = new Livreur();
        livreur.setId(1L);
        livreur.setUtilisateur(utilisateur);
        livreur.setNom("LivreurTest");
        livreur.setPrenom("PrenomTest");
        livreur.setStatut(StatutLivreur.valueOf("DISPONIBLE"));
    }

    @Test
    @WithMockUser(username = "livreur@test.com", roles = "LIVREUR")
    void testUpdateMyStatus() throws Exception {
        String newStatus = "OCCUPE";
        
        Livreur updatedLivreur = new Livreur();
        updatedLivreur.setId(1L);
        updatedLivreur.setUtilisateur(utilisateur);
        updatedLivreur.setStatut(StatutLivreur.valueOf(newStatus));

        when(livreurService.getLivreurByEmail("livreur@test.com")).thenReturn(Optional.of(livreur));
        when(livreurService.updateStatus(eq(1L), eq(newStatus))).thenReturn(Optional.of(updatedLivreur));

        mockMvc.perform(patch("/api/livreurs/me/statut")
        
                        .content(newStatus)
                        .contentType(MediaType.TEXT_PLAIN)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statut").value(newStatus));
    }

    @Test
    @WithMockUser(username = "livreur@test.com", roles = "LIVREUR")
    void testAccepterLivraison() throws Exception {
        Long commandeId = 100L;
        
        Commande commande = new Commande();
        commande.setId(commandeId);
        commande.setMontantTotal(50.0);
        
        Livraison livraison = new Livraison();
        livraison.setId(10L);
        livraison.setCommande(commande);
        
        Livraison updatedLivraison = new Livraison();
        updatedLivraison.setId(10L);
        updatedLivraison.setCommande(commande);
        updatedLivraison.setLivreur(livreur);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(2L);
        updatedLivraison.setStatutLivraison(statut);

        when(livreurService.getLivreurByEmail("livreur@test.com")).thenReturn(Optional.of(livreur));
        when(livraisonService.getLivraisonByCommandeId(commandeId)).thenReturn(Optional.of(livraison));
        when(livraisonService.updateLivraison(eq(10L), any(Livraison.class))).thenReturn(Optional.of(updatedLivraison));

        mockMvc.perform(post("/api/livreurs/me/commandes/{id}/accepter", commandeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commandeId));
    }

    @Test
    @WithMockUser(username = "livreur@test.com", roles = "LIVREUR")
    void testEnLivraisonCommande() throws Exception {
        Long commandeId = 100L;

        Commande commande = new Commande();
        commande.setId(commandeId);
        commande.setMontantTotal(50.0);

        Livraison livraison = new Livraison();
        livraison.setId(10L);
        livraison.setCommande(commande);
        livraison.setLivreur(livreur);

        Livraison updatedLivraison = new Livraison();
        updatedLivraison.setId(10L);
        updatedLivraison.setCommande(commande);
        updatedLivraison.setLivreur(livreur);
        StatutLivraison statut = new StatutLivraison();
        statut.setId(3L);
        updatedLivraison.setStatutLivraison(statut);

        when(livreurService.getLivreurByEmail("livreur@test.com")).thenReturn(Optional.of(livreur));
        when(livraisonService.getLivraisonByCommandeId(commandeId)).thenReturn(Optional.of(livraison));
        when(livraisonService.updateLivraison(eq(10L), any(Livraison.class))).thenReturn(Optional.of(updatedLivraison));

        mockMvc.perform(post("/api/livreurs/me/commandes/{id}/en-livraison", commandeId)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commandeId));
    }

    @Test
    @WithMockUser(username = "livreur@test.com", roles = "LIVREUR")
    void testGetMyCommandes() throws Exception {
        Commande commande = new Commande();
        commande.setId(100L);
        commande.setMontantTotal(50.0);

        Livraison livraison = new Livraison();
        livraison.setId(10L);
        livraison.setCommande(commande);
        livraison.setLivreur(livreur);

        when(livreurService.getLivreurByEmail("livreur@test.com")).thenReturn(Optional.of(livreur));
        when(livraisonService.getLivraisonsByLivreurId(livreur.getId())).thenReturn(List.of(livraison));

        mockMvc.perform(get("/api/livreurs/me/commandes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(100L));
    }
}