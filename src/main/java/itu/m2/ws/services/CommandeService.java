package itu.m2.ws.services;

import itu.m2.ws.models.Commande;
import itu.m2.ws.repositories.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    public List<Commande> getAllCommandes() {
        return commandeRepository.findAll();
    }

    public Optional<Commande> getCommandeById(Long id) {
        return commandeRepository.findById(id);
    }

    public Commande createCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    public Optional<Commande> updateCommande(Long id, Commande commandeDetails) {
        return commandeRepository.findById(id).map(commande -> {
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
