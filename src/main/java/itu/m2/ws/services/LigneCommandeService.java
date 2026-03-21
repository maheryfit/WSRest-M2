package itu.m2.ws.services;

import itu.m2.ws.models.LigneCommande;
import itu.m2.ws.repositories.LigneCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LigneCommandeService {

    @Autowired
    private LigneCommandeRepository ligneCommandeRepository;

    public List<LigneCommande> getAllLigneCommandes() {
        return ligneCommandeRepository.findAll();
    }

    public Optional<LigneCommande> getLigneCommandeById(Long id) {
        return ligneCommandeRepository.findById(id);
    }

    public LigneCommande createLigneCommande(LigneCommande ligneCommande) {
        return ligneCommandeRepository.save(ligneCommande);
    }

    public Optional<LigneCommande> updateLigneCommande(Long id, LigneCommande ligneCommandeDetails) {
        return ligneCommandeRepository.findById(id).map(ligneCommande -> {
            ligneCommande.setCommande(ligneCommandeDetails.getCommande());
            ligneCommande.setPlat(ligneCommandeDetails.getPlat());
            ligneCommande.setQuantite(ligneCommandeDetails.getQuantite());
            ligneCommande.setPrixUnitaire(ligneCommandeDetails.getPrixUnitaire());
            return ligneCommandeRepository.save(ligneCommande);
        });
    }

    public boolean deleteLigneCommande(Long id) {
        return ligneCommandeRepository.findById(id).map(ligneCommande -> {
            ligneCommandeRepository.delete(ligneCommande);
            return true;
        }).orElse(false);
    }
}
