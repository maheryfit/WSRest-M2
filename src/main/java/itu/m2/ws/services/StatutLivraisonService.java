package itu.m2.ws.services;

import itu.m2.ws.models.StatutLivraison;
import itu.m2.ws.repositories.StatutLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutLivraisonService {

    @Autowired
    private StatutLivraisonRepository statutLivraisonRepository;

    public List<StatutLivraison> getAllStatutLivraisons() {
        return statutLivraisonRepository.findAll();
    }

    public Optional<StatutLivraison> getStatutLivraisonById(Long id) {
        return statutLivraisonRepository.findById(id);
    }

    public StatutLivraison createStatutLivraison(StatutLivraison statutLivraison) {
        return statutLivraisonRepository.save(statutLivraison);
    }

    public Optional<StatutLivraison> updateStatutLivraison(Long id, StatutLivraison statutLivraisonDetails) {
        return statutLivraisonRepository.findById(id).map(statutLivraison -> {
            statutLivraison.setLibelle(statutLivraisonDetails.getLibelle());
            statutLivraison.setRang(statutLivraisonDetails.getRang());
            return statutLivraisonRepository.save(statutLivraison);
        });
    }

    public boolean deleteStatutLivraison(Long id) {
        return statutLivraisonRepository.findById(id).map(statutLivraison -> {
            statutLivraisonRepository.delete(statutLivraison);
            return true;
        }).orElse(false);
    }
}
