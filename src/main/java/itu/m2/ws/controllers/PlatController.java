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
@RequestMapping("/api/restaurants/{restaurantId}/plats")
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

    private Plat convertToEntity(PlatDto platDto, Long restaurantId) {
        Plat plat = new Plat();
        plat.setNom(platDto.getNom());
        plat.setDescription(platDto.getDescription());
        plat.setPrix(platDto.getPrix());
        plat.setCategorie(platDto.getCategorie());
        plat.setDisponible(platDto.isDisponible());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(restaurantId);
        plat.setRestaurant(restaurant);

        return plat;
    }

    @GetMapping
    public List<PlatDto> getAllPlats(@PathVariable Long restaurantId) {
        return platService.getPlatsByRestaurantId(restaurantId).stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatDto> getPlatById(@PathVariable Long restaurantId, @PathVariable Long id) {
        return platService.getPlatByIdAndRestaurantId(id, restaurantId)
                .map(plat -> ResponseEntity.ok(convertToDto(plat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PlatDto createPlat(@PathVariable Long restaurantId, @Valid @RequestBody PlatDto platDto) {
        Plat plat = convertToEntity(platDto, restaurantId);
        return convertToDto(platService.createPlat(plat));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlatDto> updatePlat(@PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody PlatDto platDto) {
        Plat plat = convertToEntity(platDto, restaurantId);
        return platService.getPlatByIdAndRestaurantId(id, restaurantId)
                .flatMap(existingPlat -> platService.updatePlat(id, plat))
                .map(updatedPlat -> ResponseEntity.ok(convertToDto(updatedPlat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlat(@PathVariable Long restaurantId, @PathVariable Long id) {
        return platService.getPlatByIdAndRestaurantId(id, restaurantId)
                .map(plat -> {
                    platService.deletePlat(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
