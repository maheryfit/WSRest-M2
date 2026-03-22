package itu.m2.ws.controllers;

import itu.m2.ws.dto.UtilisateurDto;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    private UtilisateurDto convertToDto(Utilisateur utilisateur) {
        // Note: Do not expose password hash
        return new UtilisateurDto(
                utilisateur.getId(),
                utilisateur.getEmail(),
                null, // Do not send password back
                utilisateur.getRole(),
                utilisateur.isActif(),
                utilisateur.getDateCreation()
        );
    }

    private Utilisateur convertToEntity(UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setEmail(utilisateurDto.getEmail());
        // In a real app, you'd hash the password here before saving
        utilisateur.setMotDePasseHash(utilisateurDto.getMotDePasse());
        utilisateur.setRole(utilisateurDto.getRole());
        utilisateur.setActif(utilisateurDto.isActif());
        return utilisateur;
    }

    @GetMapping
    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtilisateurDto> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id)
                .map(utilisateur -> ResponseEntity.ok(convertToDto(utilisateur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UtilisateurDto createUtilisateur(@Valid @RequestBody UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = convertToEntity(utilisateurDto);
        return convertToDto(utilisateurService.createUtilisateur(utilisateur));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtilisateurDto> updateUtilisateur(@PathVariable Long id, @Valid @RequestBody UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = convertToEntity(utilisateurDto);
        return utilisateurService.updateUtilisateur(id, utilisateur)
                .map(updatedUtilisateur -> ResponseEntity.ok(convertToDto(updatedUtilisateur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        return utilisateurService.deleteUtilisateur(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
