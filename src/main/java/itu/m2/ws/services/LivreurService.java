package itu.m2.ws.services;

import itu.m2.ws.models.Livreur;
import itu.m2.ws.repositories.LivreurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivreurService {

    @Autowired
    private LivreurRepository livreurRepository;

    public List<Livreur> getAllLivreurs() {
        return livreurRepository.findAll();
    }

    public Optional<Livreur> getLivreurById(Long id) {
        return livreurRepository.findById(id);
    }

    public Livreur createLivreur(Livreur livreur) {
        return livreurRepository.save(livreur);
    }

    public Optional<Livreur> updateLivreur(Long id, Livreur livreurDetails) {
        return livreurRepository.findById(id).map(livreur -> {
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
            return true;
        }).orElse(false);
    }
}
