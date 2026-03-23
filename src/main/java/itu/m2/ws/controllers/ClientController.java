package itu.m2.ws.controllers;

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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AdresseService adresseService;

    private ClientDto convertToDto(Client client) {
        ClientDto dto = new ClientDto();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setTelephone(client.getTelephone());
        dto.setEmail(client.getUtilisateur().getEmail());
        return dto;
    }

    private AdresseDto convertAdresseToDto(Adresse adresse) {
        return new AdresseDto(
                adresse.getId(),
                adresse.getClient().getId(),
                adresse.getLibelle(),
                adresse.getRue(),
                adresse.getVille(),
                adresse.getCodePostal(),
                adresse.getLatitude(),
                adresse.getLongitude(),
                adresse.isParDefaut()
        );
    }

    private Adresse convertAdresseToEntity(AdresseDto adresseDto, Client client) {
        Adresse adresse = new Adresse();
        adresse.setLibelle(adresseDto.getLibelle());
        adresse.setRue(adresseDto.getRue());
        adresse.setVille(adresseDto.getVille());
        adresse.setCodePostal(adresseDto.getCodePostal());
        adresse.setLatitude(adresseDto.getLatitude());
        adresse.setLongitude(adresseDto.getLongitude());
        adresse.setParDefaut(adresseDto.isParDefaut());
        adresse.setClient(client);

        return adresse;
    }

    private Client convertToEntity(ClientDto clientDto, Utilisateur utilisateur) {
        Client client = new Client();
        client.setNom(clientDto.getNom());
        client.setPrenom(clientDto.getPrenom());
        client.setTelephone(clientDto.getTelephone());
        client.setUtilisateur(utilisateur);
        return client;
    }

    private String getCurrentUserEmail() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            assert principal != null;
            return principal.toString();
        }
    }

    @GetMapping
    public List<ClientDto> getAllClients() {
        return clientService.getAllClients().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/me")
    public ResponseEntity<ClientDto> getMyProfile() {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> ResponseEntity.ok(convertToDto(client)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me")
    public ResponseEntity<ClientDto> updateMyProfile(@Valid @RequestBody ClientDto clientDto) {
        String email = getCurrentUserEmail();

        return clientService.getClientByEmail(email)
                .map(existingClient -> {
                    Utilisateur utilisateurToUpdate = existingClient.getUtilisateur();
                    utilisateurToUpdate.setEmail(clientDto.getEmail());
                    // Password update logic should be handled carefully, maybe in a separate endpoint

                    Client clientToUpdate = convertToEntity(clientDto, utilisateurToUpdate);
                    clientToUpdate.setId(existingClient.getId());

                    return clientService.updateClient(existingClient.getId(), clientToUpdate)
                            .map(updatedClient -> ResponseEntity.ok(convertToDto(updatedClient)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/adresses")
    public ResponseEntity<List<AdresseDto>> getMyAdresses() {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> {
                    List<Adresse> adresses = adresseService.getAllAdresses().stream()
                            .filter(a -> a.getClient().getId().equals(client.getId()))
                            .toList();
                    return ResponseEntity.ok(adresses.stream().map(this::convertAdresseToDto).collect(Collectors.toList()));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/adresses")
    public ResponseEntity<AdresseDto> createMyAdresse(@Valid @RequestBody AdresseDto adresseDto) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> {
                    Adresse adresse = convertAdresseToEntity(adresseDto, client);
                    return ResponseEntity.ok(convertAdresseToDto(adresseService.createAdresse(adresse)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/me/adresses/{id}")
    public ResponseEntity<AdresseDto> getMyAdresseById(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> adresseService.getAdresseById(id)
                        .filter(adresse -> adresse.getClient().getId().equals(client.getId()))
                        .map(adresse -> ResponseEntity.ok(convertAdresseToDto(adresse)))
                        .orElse(ResponseEntity.notFound().build())
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/me/adresses/{id}")
    public ResponseEntity<AdresseDto> updateMyAdresse(@PathVariable Long id, @Valid @RequestBody AdresseDto adresseDto) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> adresseService.getAdresseById(id)
                        .filter(adresse -> adresse.getClient().getId().equals(client.getId()))
                        .map(existingAdresse -> {
                            Adresse adresseToUpdate = convertAdresseToEntity(adresseDto, client);
                            return adresseService.updateAdresse(id, adresseToUpdate)
                                    .map(updatedAdresse -> ResponseEntity.ok(convertAdresseToDto(updatedAdresse)))
                                    .orElse(ResponseEntity.notFound().build());
                        })
                        .orElse(ResponseEntity.notFound().build())
                )
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/me/adresses/{id}")
    public ResponseEntity<Void> deleteMyAdresse(@PathVariable Long id) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email)
                .map(client -> adresseService.getAdresseById(id)
                        .filter(adresse -> adresse.getClient().getId().equals(client.getId()))
                        .map(adresse -> {
                            adresseService.deleteAdresse(id);
                            return ResponseEntity.ok().<Void>build();
                        })
                        .orElse(ResponseEntity.notFound().build())
                )
                .orElse(ResponseEntity.notFound().build());
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
