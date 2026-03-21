package itu.m2.ws.services;

import itu.m2.ws.models.StatutCommande;
import itu.m2.ws.repositories.StatutCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatutCommandeService {

    @Autowired
    private StatutCommandeRepository statutCommandeRepository;

    public List<StatutCommande> getAllStatutCommandes() {
        return statutCommandeRepository.findAll();
    }

    public Optional<StatutCommande> getStatutCommandeById(Long id) {
        return statutCommandeRepository.findById(id);
    }

    public StatutCommande createStatutCommande(StatutCommande statutCommande) {
        return statutCommandeRepository.save(statutCommande);
    }

    public Optional<StatutCommande> updateStatutCommande(Long id, StatutCommande statutCommandeDetails) {
        return statutCommandeRepository.findById(id).map(statutCommande -> {
            statutCommande.setLibelle(statutCommandeDetails.getLibelle());
            statutCommande.setRang(statutCommandeDetails.getRang());
            return statutCommandeRepository.save(statutCommande);
        });
    }

    public boolean deleteStatutCommande(Long id) {
        return statutCommandeRepository.findById(id).map(statutCommande -> {
            statutCommandeRepository.delete(statutCommande);
            return true;
        }).orElse(false);
    }
}
