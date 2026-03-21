package itu.m2.ws.services;

import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    public Optional<Utilisateur> getUtilisateurById(Long id) {
        return utilisateurRepository.findById(id);
    }

    public Utilisateur createUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Optional<Utilisateur> updateUtilisateur(Long id, Utilisateur utilisateurDetails) {
        return utilisateurRepository.findById(id).map(utilisateur -> {
            utilisateur.setEmail(utilisateurDetails.getEmail());
            utilisateur.setMotDePasseHash(utilisateurDetails.getMotDePasseHash());
            utilisateur.setRole(utilisateurDetails.getRole());
            utilisateur.setActif(utilisateurDetails.isActif());
            return utilisateurRepository.save(utilisateur);
        });
    }

    public boolean deleteUtilisateur(Long id) {
        return utilisateurRepository.findById(id).map(utilisateur -> {
            utilisateurRepository.delete(utilisateur);
            return true;
        }).orElse(false);
    }
}
