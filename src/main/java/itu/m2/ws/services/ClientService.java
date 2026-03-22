package itu.m2.ws.services;

import itu.m2.ws.models.Client;
import itu.m2.ws.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Optional<Client> updateClient(Long id, Client clientDetails) {
        return clientRepository.findById(id).map(client -> {
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
            return true;
        }).orElse(false);
    }
}
