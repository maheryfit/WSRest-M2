package itu.m2.ws.services;

import itu.m2.ws.models.Livraison;
import itu.m2.ws.models.HistoriqueLivraison;
import itu.m2.ws.repositories.LivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LivraisonService {

    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private HistoriqueLivraisonService historiqueLivraisonService;

    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findAll();
    }

    public List<Livraison> getLivraisonsByLivreurId(Long livreurId) {
        return livraisonRepository.findByLivreurId(livreurId);
    }

    public Optional<Livraison> getLivraisonByCommandeId(Long commandeId) {
        return livraisonRepository.findByCommandeId(commandeId);
    }

    public Optional<Livraison> getLivraisonById(Long id) {
        return livraisonRepository.findById(id);
    }

    @Transactional
    public Livraison createLivraison(Livraison livraison) {
        Livraison savedLivraison = livraisonRepository.save(livraison);
        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setLivraison(savedLivraison);
        historique.setStatutLivraison(savedLivraison.getStatutLivraison());
        historiqueLivraisonService.createHistoriqueLivraison(historique);
        return savedLivraison;
    }

    @Transactional
    public Optional<Livraison> updateLivraison(Long id, Livraison livraisonDetails) {
        return livraisonRepository.findById(id).map(livraison -> {
            if (!livraison.getStatutLivraison().getId().equals(livraisonDetails.getStatutLivraison().getId())) {
                HistoriqueLivraison historique = new HistoriqueLivraison();
                historique.setLivraison(livraison);
                historique.setStatutLivraison(livraisonDetails.getStatutLivraison());
                historiqueLivraisonService.createHistoriqueLivraison(historique);
            }
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
