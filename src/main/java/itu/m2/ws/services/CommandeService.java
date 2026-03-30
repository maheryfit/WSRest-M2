package itu.m2.ws.services;

import itu.m2.ws.models.Commande;
import itu.m2.ws.models.HistoriqueCommande;
import itu.m2.ws.models.LigneCommande;
import itu.m2.ws.models.Plat;
import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.repositories.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private HistoriqueCommandeService historiqueCommandeService;

    @Autowired
    private PlatService platService;

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public List<Commande> getCommandesByClientId(Long clientId) {
        return commandeRepository.findByClientId(clientId);
    }

    public List<Commande> getCommandesByRestaurantId(Long restaurantId) {
        return commandeRepository.findByRestaurantId(restaurantId);
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    @Transactional
    public Commande createCommande(Commande commande) {
        if (commande.getLignesCommandes() != null) {
            for (LigneCommande l : commande.getLignesCommandes()) {
                l.setCommande(commande);
                if (l.getPlat() != null && l.getPlat().getId() != null) {
                    Plat plat = platService.getPlatById(l.getPlat().getId())
                            .orElseThrow(() -> new IllegalArgumentException("Plat non trouvé"));
                    l.setPrixUnitaire(plat.getPrix());
                }
            }
        }
        commande.calculerMontantTotal();
        Commande savedCommande = commandeRepository.save(commande);
        HistoriqueCommande historique = new HistoriqueCommande();
        historique.setCommande(savedCommande);
        historique.setStatutCommande(savedCommande.getStatutCommande());
        historiqueCommandeService.createHistoriqueCommande(historique);
        return savedCommande;
    }

    @Transactional
    public Optional<Commande> updateCommande(Long id, Commande commandeDetails) {
        return commandeRepository.findById(id).map(commande -> {
            if (!commande.getStatutCommande().getId().equals(commandeDetails.getStatutCommande().getId())) {
                HistoriqueCommande historique = new HistoriqueCommande();
                historique.setCommande(commande);
                historique.setStatutCommande(commandeDetails.getStatutCommande());
                historiqueCommandeService.createHistoriqueCommande(historique);
            }
            commande.setClient(commandeDetails.getClient());
            commande.setRestaurant(commandeDetails.getRestaurant());
            commande.setStatutCommande(commandeDetails.getStatutCommande());
            commande.setModePaiement(commandeDetails.getModePaiement());

            if (commandeDetails.getLignesCommandes() != null) {
                commande.getLignesCommandes().clear();
                for (LigneCommande l : commandeDetails.getLignesCommandes()) {
                    l.setCommande(commande);
                    if (l.getPlat() != null && l.getPlat().getId() != null) {
                        Plat plat = platService.getPlatById(l.getPlat().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Plat non trouvé"));
                        l.setPrixUnitaire(plat.getPrix());
                    }
                    commande.getLignesCommandes().add(l);
                }
            }
            commande.calculerMontantTotal();
            return commandeRepository.save(commande);
        });
    }

    @Transactional
    public Optional<Commande> updateStatutCommande(Long id, StatutCommande statutCommande) {
        return commandeRepository.findById(id).map(commande -> {
            if (!commande.getStatutCommande().getId().equals(statutCommande.getId())) {
                HistoriqueCommande historique = new HistoriqueCommande();
                historique.setCommande(commande);
                historique.setStatutCommande(statutCommande);
                historiqueCommandeService.createHistoriqueCommande(historique);
            }
            commande.setStatutCommande(statutCommande);
            return commandeRepository.save(commande);
        });
    }

    public boolean deleteCommande(Long id) {
        return commandeRepository.findById(id).map(commande -> {
            commandeRepository.delete(commande);
            return true;
        }).orElse(false);
    }

    @Transactional
    public Optional<Commande> ajouterLigneCommande(Long idCommande, LigneCommande ligne) {
        return commandeRepository.findById(idCommande).map(commande -> {
            if (ligne.getPlat() != null && ligne.getPlat().getId() != null) {
                Plat plat = platService.getPlatById(ligne.getPlat().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Plat non trouvé"));
                ligne.setPrixUnitaire(plat.getPrix());
            }
            ligne.setCommande(commande);
            commande.getLignesCommandes().add(ligne);
            commande.calculerMontantTotal();
            return commandeRepository.save(commande);
        });
    }

    @Transactional
    public Optional<Commande> supprimerLigneCommande(Long idCommande, Long idLigne) {
        return commandeRepository.findById(idCommande).map(commande -> {
            boolean removed = commande.getLignesCommandes()
                    .removeIf(l -> l.getId() != null && l.getId().equals(idLigne));
            if (removed) {
                commande.calculerMontantTotal();
                return commandeRepository.save(commande);
            }
            return commande;
        });
    }
}
