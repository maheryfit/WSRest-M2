package itu.m2.ws.services;

import itu.m2.ws.models.Commande;
import itu.m2.ws.models.HistoriqueCommande;
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

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    @Transactional
    public Commande createCommande(Commande commande) {
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
            commande.setMontantTotal(commandeDetails.getMontantTotal());
            commande.setModePaiement(commandeDetails.getModePaiement());
            return commandeRepository.save(commande);
        });
    }

    public boolean deleteCommande(Long id) {
        return commandeRepository.findById(id).map(commande -> {
            commandeRepository.delete(commande);
            return true;
        }).orElse(false);
    }
}
