package itu.m2.ws.services;

import itu.m2.ws.models.Paiement;
import itu.m2.ws.repositories.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepository.findById(id);
    }

    public Paiement createPaiement(Paiement paiement) {
        return paiementRepository.save(paiement);
    }

    public Optional<Paiement> updatePaiement(Long id, Paiement paiementDetails) {
        return paiementRepository.findById(id).map(paiement -> {
            paiement.setCommande(paiementDetails.getCommande());
            paiement.setMontant(paiementDetails.getMontant());
            paiement.setStatutPaiement(paiementDetails.getStatutPaiement());
            paiement.setDatePaiement(paiementDetails.getDatePaiement());
            return paiementRepository.save(paiement);
        });
    }

    public boolean deletePaiement(Long id) {
        return paiementRepository.findById(id).map(paiement -> {
            paiementRepository.delete(paiement);
            return true;
        }).orElse(false);
    }
}
