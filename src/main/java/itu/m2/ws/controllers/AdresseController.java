package itu.m2.ws.controllers;

import itu.m2.ws.dto.AdresseDto;
import itu.m2.ws.models.Adresse;
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

    @GetMapping
    public List<AdresseDto> getAllAdresses() {
        return adresseService.getAllAdresses().stream().map(AdresseDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdresseDto> getAdresseById(@PathVariable Long id) {
        return adresseService.getAdresseById(id)
                .map(adresse -> ResponseEntity.ok(AdresseDto.convertToDto(adresse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AdresseDto createAdresse(@Valid @RequestBody AdresseDto adresseDto) {
        Adresse adresse = AdresseDto.convertToEntity(adresseDto);
        return AdresseDto.convertToDto(adresseService.createAdresse(adresse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdresseDto> updateAdresse(@PathVariable Long id, @Valid @RequestBody AdresseDto adresseDto) {
        Adresse adresse = AdresseDto.convertToEntity(adresseDto);
        return adresseService.updateAdresse(id, adresse)
                .map(updatedAdresse -> ResponseEntity.ok(AdresseDto.convertToDto(updatedAdresse)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdresse(@PathVariable Long id) {
        return adresseService.deleteAdresse(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
