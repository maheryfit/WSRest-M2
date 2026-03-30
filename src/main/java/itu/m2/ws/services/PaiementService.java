package itu.m2.ws.services;

import itu.m2.ws.models.Paiement;
import itu.m2.ws.models.HistoriquePaiement;
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

    @Autowired
    private StatutPaiementService statutPaiementService;

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
            if (paiementDetails.getStatutPaiement() != null
                    && !paiement.getStatutPaiement().getId().equals(paiementDetails.getStatutPaiement().getId())) {
                HistoriquePaiement historique = new HistoriquePaiement();
                historique.setPaiement(paiement);
                historique.setStatutPaiement(paiementDetails.getStatutPaiement());
                historiquePaiementService.createHistoriquePaiement(historique);
            }
            if (paiementDetails.getCommande() != null)
                paiement.setCommande(paiementDetails.getCommande());
            if (paiementDetails.getMontant() > 0)
                paiement.setMontant(paiementDetails.getMontant());
            if (paiementDetails.getStatutPaiement() != null)
                paiement.setStatutPaiement(paiementDetails.getStatutPaiement());
            if (paiementDetails.getDatePaiement() != null)
                paiement.setDatePaiement(paiementDetails.getDatePaiement());
            return paiementRepository.save(paiement);
        });
    }

    @Transactional
    public Optional<Paiement> updateStatutPaiement(Long id, String libelleStatut) {
        return paiementRepository.findById(id)
                .flatMap(paiement -> statutPaiementService.getStatutPaiementByLibelle(libelleStatut).map(statut -> {
                    if (!paiement.getStatutPaiement().getId().equals(statut.getId())) {
                        paiement.setStatutPaiement(statut);
                        Paiement updated = paiementRepository.save(paiement);

                        HistoriquePaiement historique = new HistoriquePaiement();
                        historique.setPaiement(updated);
                        historique.setStatutPaiement(statut);
                        historiquePaiementService.createHistoriquePaiement(historique);
                        return updated;
                    }
                    return paiement;
                }));
    }

    public boolean deletePaiement(Long id) {
        return paiementRepository.findById(id).map(paiement -> {
            paiementRepository.delete(paiement);
            return true;
        }).orElse(false);
    }
}
