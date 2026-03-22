package itu.m2.ws.controllers;

import itu.m2.ws.dto.PlatDto;
import itu.m2.ws.models.Plat;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.services.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plats")
public class PlatController {

    @Autowired
    private PlatService platService;

    private PlatDto convertToDto(Plat plat) {
        return new PlatDto(
                plat.getId(),
                plat.getRestaurant().getId(),
                plat.getNom(),
                plat.getDescription(),
                plat.getPrix(),
                plat.getCategorie(),
                plat.isDisponible()
        );
    }

    private Plat convertToEntity(PlatDto platDto) {
        Plat plat = new Plat();
        plat.setNom(platDto.getNom());
        plat.setDescription(platDto.getDescription());
        plat.setPrix(platDto.getPrix());
        plat.setCategorie(platDto.getCategorie());
        plat.setDisponible(platDto.isDisponible());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(platDto.getRestaurantId());
        plat.setRestaurant(restaurant);

        return plat;
    }

    @GetMapping
    public List<PlatDto> getAllPlats() {
        return platService.getAllPlats().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatDto> getPlatById(@PathVariable Long id) {
        return platService.getPlatById(id)
                .map(plat -> ResponseEntity.ok(convertToDto(plat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PlatDto createPlat(@Valid @RequestBody PlatDto platDto) {
        Plat plat = convertToEntity(platDto);
        return convertToDto(platService.createPlat(plat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatDto> updatePlat(@PathVariable Long id, @Valid @RequestBody PlatDto platDto) {
        Plat plat = convertToEntity(platDto);
        return platService.updatePlat(id, plat)
                .map(updatedPlat -> ResponseEntity.ok(convertToDto(updatedPlat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlat(@PathVariable Long id) {
        return platService.deletePlat(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
