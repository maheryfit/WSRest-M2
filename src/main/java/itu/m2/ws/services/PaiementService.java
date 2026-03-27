package itu.m2.ws.services;

import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.HistoriquePaiement;
import itu.m2.ws.models.StatutPaiement;
import itu.m2.ws.repositories.PaiementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PaiementService {

    @Autowired
    private PaiementRepository paiementRepository;

    @Autowired
    private HistoriquePaiementService historiquePaiementService;

    public List<Paiement> getAllPaiements() {
        return paiementRepository.findAll();
    }

    public Optional<Paiement> getPaiementById(Long id) {
        return paiementRepository.findById(id);
    }

    public Optional<Paiement> getPaiementByCommandeId(Long commandeId) {
        return paiementRepository.findByCommandeId(commandeId);
    }

    @Transactional
    public Paiement createPaiement(Paiement paiement) {
        Paiement savedPaiement = paiementRepository.save(paiement);
        HistoriquePaiement historique = new HistoriquePaiement();
        historique.setPaiement(savedPaiement);
        historique.setStatutPaiement(savedPaiement.getStatutPaiement());
        historiquePaiementService.createHistoriquePaiement(historique);
        return savedPaiement;
    }

    @Transactional
    public Optional<Paiement> updatePaiement(Long id, Paiement paiementDetails) {
        return paiementRepository.findById(id).map(paiement -> {
            if (!paiement.getStatutPaiement().getId().equals(paiementDetails.getStatutPaiement().getId())) {
                HistoriquePaiement historique = new HistoriquePaiement();
                historique.setPaiement(paiement);
                historique.setStatutPaiement(paiementDetails.getStatutPaiement());
                historiquePaiementService.createHistoriquePaiement(historique);
            }
            paiement.setCommande(paiementDetails.getCommande());
            paiement.setMontant(paiementDetails.getMontant());
            paiement.setStatutPaiement(paiementDetails.getStatutPaiement());
            paiement.setDatePaiement(paiementDetails.getDatePaiement());
            return paiementRepository.save(paiement);
        });
    }

    @Transactional
    public Optional<Paiement> updateStatutPaiement(Long id, StatutPaiement statutPaiement) {
        return paiementRepository.findById(id).map(paiement -> {
            if (!paiement.getStatutPaiement().getId().equals(statutPaiement.getId())) {
                HistoriquePaiement historique = new HistoriquePaiement();
                historique.setPaiement(paiement);
                historique.setStatutPaiement(statutPaiement);
                historiquePaiementService.createHistoriquePaiement(historique);
            }
            paiement.setStatutPaiement(statutPaiement);
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
