package itu.m2.ws.services;

import itu.m2.ws.models.Livraison;
import itu.m2.ws.repositories.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivraisonService {

    @Autowired
    private LivraisonRepository livraisonRepository;

    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    public Optional<Livraison> getLivraisonById(Long id) {
        return livraisonRepository.findById(id);
    }

    public Livraison createLivraison(Livraison livraison) {
        return livraisonRepository.save(livraison);
    }

    public Optional<Livraison> updateLivraison(Long id, Livraison livraisonDetails) {
        return livraisonRepository.findById(id).map(livraison -> {
            livraison.setCommande(livraisonDetails.getCommande());
            livraison.setLivreur(livraisonDetails.getLivreur());
            livraison.setAdresseLivraison(livraisonDetails.getAdresseLivraison());
            livraison.setDateLivraisonEstimee(livraisonDetails.getDateLivraisonEstimee());
            livraison.setDateLivraisonReelle(livraisonDetails.getDateLivraisonReelle());
            livraison.setStatutLivraison(livraisonDetails.getStatutLivraison());
            return livraisonRepository.save(livraison);
        });
    }

    public boolean deleteLivraison(Long id) {
        return livraisonRepository.findById(id).map(livraison -> {
            livraisonRepository.delete(livraison);
            return true;
        }).orElse(false);
    }
}
