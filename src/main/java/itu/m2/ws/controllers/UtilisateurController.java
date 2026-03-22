package itu.m2.ws.controllers;

import itu.m2.ws.dto.LoginRequestDto;
import itu.m2.ws.dto.UtilisateurDto;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
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
        utilisateur.setMotDePasseHash(utilisateurDto.getMotDePasse());
        utilisateur.setRole(utilisateurDto.getRole());
        utilisateur.setActif(utilisateurDto.isActif());
        return utilisateur;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto loginRequest) {
        try {
            Utilisateur utilisateur = utilisateurService.logIn(loginRequest.getEmail(), loginRequest.getMotDePasse());
            String token = utilisateurService.generateToken(utilisateur.getEmail());
            return ResponseEntity.ok(token);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logOut() {
        SecurityContextHolder.getContext().setAuthentication(null);
        return ResponseEntity.status(HttpStatus.OK).body("Logout successfully");
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
