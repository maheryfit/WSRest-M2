package itu.m2.ws.services;

import itu.m2.ws.models.HistoriqueLivraison;
import itu.m2.ws.repositories.HistoriqueLivraisonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueLivraisonService {

    @Autowired
    private HistoriqueLivraisonRepository historiqueLivraisonRepository;

    public List<HistoriqueLivraison> getAllHistoriqueLivraisons() {
        return historiqueLivraisonRepository.findAll();
    }

    public Optional<HistoriqueLivraison> getHistoriqueLivraisonById(Long id) {
        return historiqueLivraisonRepository.findById(id);
    }

    public HistoriqueLivraison createHistoriqueLivraison(HistoriqueLivraison historiqueLivraison) {
        return historiqueLivraisonRepository.save(historiqueLivraison);
    }

    public Optional<HistoriqueLivraison> updateHistoriqueLivraison(Long id, HistoriqueLivraison historiqueLivraisonDetails) {
        return historiqueLivraisonRepository.findById(id).map(historiqueLivraison -> {
            historiqueLivraison.setLivraison(historiqueLivraisonDetails.getLivraison());
            historiqueLivraison.setDateStatus(historiqueLivraisonDetails.getDateStatus());
            historiqueLivraison.setStatutLivraison(historiqueLivraisonDetails.getStatutLivraison());
            return historiqueLivraisonRepository.save(historiqueLivraison);
        });
    }

    public boolean deleteHistoriqueLivraison(Long id) {
        return historiqueLivraisonRepository.findById(id).map(historiqueLivraison -> {
            historiqueLivraisonRepository.delete(historiqueLivraison);
            return true;
        }).orElse(false);
    }
}
