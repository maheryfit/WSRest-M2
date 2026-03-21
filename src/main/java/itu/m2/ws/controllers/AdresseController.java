package itu.m2.ws.controllers;

import itu.m2.ws.dto.AdresseDto;
import itu.m2.ws.models.Adresse;
import itu.m2.ws.models.Client;
import itu.m2.ws.services.AdresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/adresses")
public class AdresseController {

    @Autowired
    private AdresseService adresseService;

    private AdresseDto convertToDto(Adresse adresse) {
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

    private Adresse convertToEntity(AdresseDto adresseDto) {
        Adresse adresse = new Adresse();
        adresse.setLibelle(adresseDto.getLibelle());
        adresse.setRue(adresseDto.getRue());
        adresse.setVille(adresseDto.getVille());
        adresse.setCodePostal(adresseDto.getCodePostal());
        adresse.setLatitude(adresseDto.getLatitude());
        adresse.setLongitude(adresseDto.getLongitude());
        adresse.setParDefaut(adresseDto.isParDefaut());

        Client client = new Client();
        client.setId(adresseDto.getClientId());
        adresse.setClient(client);

        return adresse;
    }

    @GetMapping
    public List<AdresseDto> getAllAdresses() {
        return adresseService.getAllAdresses().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdresseDto> getAdresseById(@PathVariable Long id) {
        return adresseService.getAdresseById(id)
                .map(adresse -> ResponseEntity.ok(convertToDto(adresse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AdresseDto createAdresse(@Valid @RequestBody AdresseDto adresseDto) {
        Adresse adresse = convertToEntity(adresseDto);
        return convertToDto(adresseService.createAdresse(adresse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdresseDto> updateAdresse(@PathVariable Long id, @Valid @RequestBody AdresseDto adresseDto) {
        Adresse adresse = convertToEntity(adresseDto);
        return adresseService.updateAdresse(id, adresse)
                .map(updatedAdresse -> ResponseEntity.ok(convertToDto(updatedAdresse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdresse(@PathVariable Long id) {
        return adresseService.deleteAdresse(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
