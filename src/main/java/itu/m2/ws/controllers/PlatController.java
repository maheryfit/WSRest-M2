package itu.m2.ws.controllers;

import itu.m2.ws.dto.PlatDto;
import itu.m2.ws.models.Plat;
import itu.m2.ws.services.PlatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/restaurants/{restaurantId}/plats")
public class PlatController {

    @Autowired
    private PlatService platService;

    @GetMapping
    public List<PlatDto> getAllPlats(@PathVariable Long restaurantId) {
        return platService.getPlatsByRestaurantId(restaurantId).stream().map(PlatDto::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlatDto> getPlatById(@PathVariable Long restaurantId, @PathVariable Long id) {
        return platService.getPlatByIdAndRestaurantId(id, restaurantId)
                .map(plat -> ResponseEntity.ok(PlatDto.convertToDto(plat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public PlatDto createPlat(@PathVariable Long restaurantId, @Valid @RequestBody PlatDto platDto) {
        Plat plat = PlatDto.convertToEntity(platDto, restaurantId);
        return PlatDto.convertToDto(platService.createPlat(plat));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public ResponseEntity<PlatDto> updatePlat(@PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody PlatDto platDto) {
        Plat plat = PlatDto.convertToEntity(platDto, restaurantId);
        return platService.getPlatByIdAndRestaurantId(id, restaurantId)
                .flatMap(existingPlat -> platService.updatePlat(id, plat))
                .map(updatedPlat -> ResponseEntity.ok(PlatDto.convertToDto(updatedPlat)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('RESTAURANT')")
    public ResponseEntity<Void> deletePlat(@PathVariable Long restaurantId, @PathVariable Long id) {
        return platService.getPlatByIdAndRestaurantId(id, restaurantId)
                .map(plat -> {
                    platService.deletePlat(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
