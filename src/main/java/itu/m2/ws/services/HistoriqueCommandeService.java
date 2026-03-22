package itu.m2.ws.services;

import itu.m2.ws.models.HistoriqueCommande;
import itu.m2.ws.repositories.HistoriqueCommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoriqueCommandeService {

    @Autowired
    private HistoriqueCommandeRepository historiqueCommandeRepository;

    public List<HistoriqueCommande> getAllHistoriqueCommandes() {
        return historiqueCommandeRepository.findAll();
    }

    public Optional<HistoriqueCommande> getHistoriqueCommandeById(Long id) {
        return historiqueCommandeRepository.findById(id);
    }

    public HistoriqueCommande createHistoriqueCommande(HistoriqueCommande historiqueCommande) {
        return historiqueCommandeRepository.save(historiqueCommande);
    }

    public Optional<HistoriqueCommande> updateHistoriqueCommande(Long id, HistoriqueCommande historiqueCommandeDetails) {
        return historiqueCommandeRepository.findById(id).map(historiqueCommande -> {
            historiqueCommande.setCommande(historiqueCommandeDetails.getCommande());
            historiqueCommande.setDateStatus(historiqueCommandeDetails.getDateStatus());
            historiqueCommande.setStatutCommande(historiqueCommandeDetails.getStatutCommande());
            return historiqueCommandeRepository.save(historiqueCommande);
        });
    }

    public boolean deleteHistoriqueCommande(Long id) {
        return historiqueCommandeRepository.findById(id).map(historiqueCommande -> {
            historiqueCommandeRepository.delete(historiqueCommande);
            return true;
        }).orElse(false);
    }
}
