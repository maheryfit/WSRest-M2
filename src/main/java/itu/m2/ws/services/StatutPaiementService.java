package itu.m2.ws.services;

import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.repositories.StatutPaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutPaiementService {

    @Autowired
    private StatutPaiementRepository statutPaiementRepository;

    public List<StatutPaiement> getAllStatutPaiements() {
        return statutPaiementRepository.findAll();
    }

    public Optional<StatutPaiement> getStatutPaiementById(Long id) {
        return statutPaiementRepository.findById(id);
    }

    public StatutPaiement createStatutPaiement(StatutPaiement statutPaiement) {
        return statutPaiementRepository.save(statutPaiement);
    }

    public Optional<StatutPaiement> updateStatutPaiement(Long id, StatutPaiement statutPaiementDetails) {
        return statutPaiementRepository.findById(id).map(statutPaiement -> {
            statutPaiement.setLibelle(statutPaiementDetails.getLibelle());
            statutPaiement.setRang(statutPaiementDetails.getRang());
            return statutPaiementRepository.save(statutPaiement);
        });
    }

    public boolean deleteStatutPaiement(Long id) {
        return statutPaiementRepository.findById(id).map(statutPaiement -> {
            statutPaiementRepository.delete(statutPaiement);
            return true;
        }).orElse(false);
    }
}
