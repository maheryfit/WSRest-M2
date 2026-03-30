package itu.m2.ws.controllers;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import itu.m2.ws.dto.AdresseDto;
import itu.m2.ws.dto.ClientDto;
import itu.m2.ws.models.Adresse;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.enums.Role;
import itu.m2.ws.services.AdresseService;
import itu.m2.ws.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@SecurityRequirement(name = "bearerAuth")
public class ClientController extends BaseController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AdresseService adresseService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients().stream().map(ClientDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<ClientDto> getMyProfile() {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> ResponseEntity.ok(ClientDto.convertToDto(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<ClientDto> updateMyProfile(@Valid @RequestBody ClientDto clientDto) {
        String email = getCurrentUserEmail();

        return clientService.getClientByEmail(email)
                .map(existingClient -> {
                    Utilisateur utilisateurToUpdate = existingClient.getUtilisateur();
                    utilisateurToUpdate.setEmail(clientDto.getEmail());
                    Client clientToUpdate = ClientDto.convertToEntity(clientDto, utilisateurToUpdate);
                    clientToUpdate.setId(existingClient.getId());

                    return clientService.updateClient(existingClient.getId(), clientToUpdate)
                            .map(updatedClient -> ResponseEntity.ok(ClientDto.convertToDto(updatedClient)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/adresses")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<List<AdresseDto>> getMyAdresses() {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> {
                    List<Adresse> adresses = adresseService.getAllAdresses().stream()
                            .filter(a -> a.getClient().getId().equals(client.getId()))
                            .toList();
                    return ResponseEntity
                            .ok(adresses.stream().map(AdresseDto::convertToDto).collect(Collectors.toList()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/adresses")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<AdresseDto> createMyAdresse(@Valid @RequestBody AdresseDto adresseDto) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> {
                    Adresse adresse = AdresseDto.convertToEntity(adresseDto);
                    adresse.setClient(client); // Ensure client is set
                    return ResponseEntity.ok(AdresseDto.convertToDto(adresseService.createAdresse(adresse)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/adresses/{id}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<AdresseDto> getMyAdresseById(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> adresseService.getAdresseById(id)
                        .filter(adresse -> adresse.getClient().getId().equals(client.getId()))
                        .map(adresse -> ResponseEntity.ok(AdresseDto.convertToDto(adresse)))
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me/adresses/{id}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<AdresseDto> updateMyAdresse(@PathVariable Long id,
            @Valid @RequestBody AdresseDto adresseDto) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> adresseService.getAdresseById(id)
                        .filter(adresse -> adresse.getClient().getId().equals(client.getId()))
                        .map(existingAdresse -> {
                            Adresse adresseToUpdate = AdresseDto.convertToEntity(adresseDto);
                            adresseToUpdate.setClient(client);
                            return adresseService.updateAdresse(id, adresseToUpdate)
                                    .map(updatedAdresse -> ResponseEntity.ok(AdresseDto.convertToDto(updatedAdresse)))
                                    .orElse(ResponseEntity.notFound().build());
                        })
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/me/adresses/{id}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<Void> deleteMyAdresse(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> adresseService.getAdresseById(id)
                        .filter(adresse -> adresse.getClient().getId().equals(client.getId()))
                        .map(adresse -> {
                            adresseService.deleteAdresse(id);
                            return ResponseEntity.ok().<Void>build();
                        })
                        .orElse(ResponseEntity.notFound().build()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return clientService.getClientById(id)
                .map(client -> ResponseEntity.ok(ClientDto.convertToDto(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ClientDto createClient(@Valid @RequestBody ClientDto clientDto) {
        Utilisateur newUser = new Utilisateur();
        newUser.setEmail(clientDto.getEmail());
        newUser.setMotDePasseHash(clientDto.getMotDePasse()); // Remember to hash in a real app
        newUser.setRole(Role.CLIENT);

        Client client = ClientDto.convertToEntity(clientDto, newUser);
        return ClientDto.convertToDto(clientService.createClient(client));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDto clientDto) {
        return clientService.getClientById(id)
                .map(existingClient -> {
                    Utilisateur utilisateurToUpdate = existingClient.getUtilisateur();
                    utilisateurToUpdate.setEmail(clientDto.getEmail());
                    // Password update logic should be handled carefully, maybe in a separate
                    // endpoint

                    Client clientToUpdate = ClientDto.convertToEntity(clientDto, utilisateurToUpdate);
                    clientToUpdate.setId(id);

                    return clientService.updateClient(id, clientToUpdate)
                            .map(updatedClient -> ResponseEntity.ok(ClientDto.convertToDto(updatedClient)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        return clientService.deleteClient(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
