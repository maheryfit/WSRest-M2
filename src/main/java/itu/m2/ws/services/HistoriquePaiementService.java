package itu.m2.ws.services;

import itu.m2.ws.models.HistoriquePaiement;
import itu.m2.ws.repositories.HistoriquePaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriquePaiementService {

    @Autowired
    private HistoriquePaiementRepository historiquePaiementRepository;

    public List<HistoriquePaiement> getAllHistoriquePaiements() {
        return historiquePaiementRepository.findAll();
    }

    public Optional<HistoriquePaiement> getHistoriquePaiementById(Long id) {
        return historiquePaiementRepository.findById(id);
    }

    public HistoriquePaiement createHistoriquePaiement(HistoriquePaiement historiquePaiement) {
        return historiquePaiementRepository.save(historiquePaiement);
    }

    public Optional<HistoriquePaiement> updateHistoriquePaiement(Long id, HistoriquePaiement historiquePaiementDetails) {
        return historiquePaiementRepository.findById(id).map(historiquePaiement -> {
            historiquePaiement.setPaiement(historiquePaiementDetails.getPaiement());
            historiquePaiement.setDateStatus(historiquePaiementDetails.getDateStatus());
            historiquePaiement.setStatutPaiement(historiquePaiementDetails.getStatutPaiement());
            return historiquePaiementRepository.save(historiquePaiement);
        });
    }

    public boolean deleteHistoriquePaiement(Long id) {
        return historiquePaiementRepository.findById(id).map(historiquePaiement -> {
            historiquePaiementRepository.delete(historiquePaiement);
            return true;
        }).orElse(false);
    }
}
