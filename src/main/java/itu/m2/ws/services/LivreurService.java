package itu.m2.ws.services;

import itu.m2.ws.models.Livreur;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.repositories.LivreurRepository;
import itu.m2.ws.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LivreurService {

    @Autowired
    private LivreurRepository livreurRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }

    public Optional<Livreur> getLivreurById(Long id) {
        return livreurRepository.findById(id);
    }

    @Transactional
    public Livreur createLivreur(Livreur livreur) {
        Utilisateur savedUtilisateur = utilisateurRepository.save(livreur.getUtilisateur());
        livreur.setUtilisateur(savedUtilisateur);
        return livreurRepository.save(livreur);
    }

    @Transactional
    public Optional<Livreur> updateLivreur(Long id, Livreur livreurDetails) {
        return livreurRepository.findById(id).map(livreur -> {
            utilisateurRepository.save(livreurDetails.getUtilisateur());
            livreur.setUtilisateur(livreurDetails.getUtilisateur());
            livreur.setNom(livreurDetails.getNom());
            livreur.setPrenom(livreurDetails.getPrenom());
            livreur.setTelephone(livreurDetails.getTelephone());
            livreur.setStatut(livreurDetails.getStatut());
            return livreurRepository.save(livreur);
        });
    }

    public boolean deleteLivreur(Long id) {
        return livreurRepository.findById(id).map(livreur -> {
            livreurRepository.delete(livreur);
            utilisateurRepository.delete(livreur.getUtilisateur());
            return true;
        }).orElse(false);
    }
}
