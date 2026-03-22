package itu.m2.ws.services;

import itu.m2.ws.models.Client;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.repositories.ClientRepository;
import itu.m2.ws.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    @Transactional
    public Client createClient(Client client) {
        Utilisateur savedUtilisateur = utilisateurRepository.save(client.getUtilisateur());
        client.setUtilisateur(savedUtilisateur);
        return clientRepository.save(client);
    }

    @Transactional
    public Optional<Client> updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id).map(client -> {
            utilisateurRepository.save(clientDetails.getUtilisateur());
            client.setUtilisateur(clientDetails.getUtilisateur());
            client.setNom(clientDetails.getNom());
            client.setPrenom(clientDetails.getPrenom());
            client.setTelephone(clientDetails.getTelephone());
            return clientRepository.save(client);
        });
    }

    public boolean deleteClient(Long id) {
        return clientRepository.findById(id).map(client -> {
            clientRepository.delete(client);
            utilisateurRepository.delete(client.getUtilisateur());
            return true;
        }).orElse(false);
    }
}
