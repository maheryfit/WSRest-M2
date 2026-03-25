package itu.m2.ws.controllers;

import itu.m2.ws.dto.LoginRequestDto;
import itu.m2.ws.dto.UtilisateurDto;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<UtilisateurDto> getAllUtilisateurs() {
        return utilisateurService.getAllUtilisateurs().stream().map(UtilisateurDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UtilisateurDto> getUtilisateurById(@PathVariable Long id) {
        return utilisateurService.getUtilisateurById(id)
                .map(utilisateur -> ResponseEntity.ok(UtilisateurDto.convertToDto(utilisateur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public UtilisateurDto createUtilisateur(@Valid @RequestBody UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = UtilisateurDto.convertToEntity(utilisateurDto);
        return UtilisateurDto.convertToDto(utilisateurService.createUtilisateur(utilisateur));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<UtilisateurDto> updateUtilisateur(@PathVariable Long id, @Valid @RequestBody UtilisateurDto utilisateurDto) {
        Utilisateur utilisateur = UtilisateurDto.convertToEntity(utilisateurDto);
        return utilisateurService.updateUtilisateur(id, utilisateur)
                .map(updatedUtilisateur -> ResponseEntity.ok(UtilisateurDto.convertToDto(updatedUtilisateur)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Long id) {
        return utilisateurService.deleteUtilisateur(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
