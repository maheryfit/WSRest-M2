package itu.m2.ws.controllers;

import itu.m2.ws.dto.ClientDto;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private ClientDto convertToDto(Client client) {
        return new ClientDto(
                client.getId(),
                client.getUtilisateur().getId(),
                client.getNom(),
                client.getPrenom(),
                client.getTelephone()
        );
    }

    private Client convertToEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setNom(clientDto.getNom());
        client.setPrenom(clientDto.getPrenom());
        client.setTelephone(clientDto.getTelephone());

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(clientDto.getUtilisateurId());
        client.setUtilisateur(utilisateur);

        return client;
    }

    @GetMapping
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(client -> ResponseEntity.ok(convertToDto(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClientDto createClient(@Valid @RequestBody ClientDto clientDto) {
        Client client = convertToEntity(clientDto);
        return convertToDto(clientService.createClient(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto) {
        Client client = convertToEntity(clientDto);
        return clientService.updateClient(id, client)
                .map(updatedClient -> ResponseEntity.ok(convertToDto(updatedClient)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        return clientService.deleteClient(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
