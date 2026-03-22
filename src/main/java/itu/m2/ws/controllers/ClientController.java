package itu.m2.ws.controllers;

import itu.m2.ws.dto.ClientDto;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.ClientService;
import itu.m2.ws.services.UtilisateurService;
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
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setTelephone(client.getTelephone());
        dto.setEmail(client.getUtilisateur().getEmail());
        return dto;
    }

    private Client convertToEntity(ClientDto clientDto, Utilisateur utilisateur) {
        Client client = new Client();
        client.setNom(clientDto.getNom());
        client.setPrenom(clientDto.getPrenom());
        client.setTelephone(clientDto.getTelephone());
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
        Utilisateur newUser = new Utilisateur();
        newUser.setEmail(clientDto.getEmail());
        newUser.setMotDePasseHash(clientDto.getMotDePasse()); // Remember to hash in a real app
        newUser.setRole(Role.CLIENT);
        
        Client client = convertToEntity(clientDto, newUser);
        return convertToDto(clientService.createClient(client));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto) {
        return clientService.getClientById(id)
            .map(existingClient -> {
                Utilisateur utilisateurToUpdate = existingClient.getUtilisateur();
                utilisateurToUpdate.setEmail(clientDto.getEmail());
                // Password update logic should be handled carefully, maybe in a separate endpoint
                
                Client clientToUpdate = convertToEntity(clientDto, utilisateurToUpdate);
                clientToUpdate.setId(id);

                return clientService.updateClient(id, clientToUpdate)
                        .map(updatedClient -> ResponseEntity.ok(convertToDto(updatedClient)))
                        .orElse(ResponseEntity.notFound().build());
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        return clientService.deleteClient(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
