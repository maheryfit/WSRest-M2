package itu.m2.ws.controllers;

import itu.m2.ws.dto.AvisRestaurantDto;
import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.services.AvisRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AvisRestaurantController {

    @Autowired
    private AvisRestaurantService avisRestaurantService;

    @GetMapping("/avisrestaurants")
    public List<AvisRestaurantDto> getAllAvisRestaurants() {
        return avisRestaurantService.getAllAvisRestaurants().stream().map(AvisRestaurantDto::convertToDto).collect(Collectors.toList());
    }
    
    @GetMapping("/restaurants/{restaurantId}/avis")
    public ResponseEntity<List<AvisRestaurantDto>> getAvisByRestaurantId(@PathVariable Long restaurantId) {
        List<AvisRestaurantDto> avis = avisRestaurantService.getAvisByRestaurantId(restaurantId)
                .stream()
                .map(AvisRestaurantDto::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(avis);
    }
    
    @PostMapping("/restaurants/{restaurantId}/avis")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<AvisRestaurantDto> createAvisByRestaurantId(@PathVariable Long restaurantId, @Valid @RequestBody AvisRestaurantDto avisDto) {
        // En vrai projet, il faudrait vérifier ici (ou dans le service) si le client a une commande livrée pour ce restaurant
        AvisRestaurant avis = AvisRestaurantDto.convertToEntity(avisDto, restaurantId);
        return ResponseEntity.ok(AvisRestaurantDto.convertToDto(avisRestaurantService.createAvisRestaurant(avis)));
    }
    
    @PutMapping("/restaurants/{restaurantId}/avis/{id}")
    @PreAuthorize("hasAnyRole('CLIENT')")
    public ResponseEntity<AvisRestaurantDto> updateAvisByRestaurantId(@PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody AvisRestaurantDto avisDto) {
        // Idéalement vérifier que l'utilisateur courant est l'auteur de l'avis
        AvisRestaurant avis = AvisRestaurantDto.convertToEntity(avisDto, restaurantId);
        return avisRestaurantService.updateAvisRestaurant(id, avis)
                .map(updatedAvis -> ResponseEntity.ok(AvisRestaurantDto.convertToDto(updatedAvis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/restaurants/{restaurantId}/avis/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> deleteAvisByRestaurantId(@PathVariable Long restaurantId, @PathVariable Long id) {
        // Idéalement vérifier que l'utilisateur courant est l'auteur de l'avis s'il est CLIENT
        return avisRestaurantService.deleteAvisRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/avisrestaurants/{id}")
    public ResponseEntity<AvisRestaurantDto> getAvisRestaurantById(@PathVariable Long id) {
        return avisRestaurantService.getAvisRestaurantById(id)
                .map(avis -> ResponseEntity.ok(AvisRestaurantDto.convertToDto(avis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/avisrestaurants")
    public AvisRestaurantDto createAvisRestaurant(@Valid @RequestBody AvisRestaurantDto avisDto) {
        AvisRestaurant avis = AvisRestaurantDto.convertToEntity(avisDto, avisDto.getRestaurantId());
        return AvisRestaurantDto.convertToDto(avisRestaurantService.createAvisRestaurant(avis));
    }

    @PutMapping("/avisrestaurants/{id}")
    public ResponseEntity<AvisRestaurantDto> updateAvisRestaurant(@PathVariable Long id, @Valid @RequestBody AvisRestaurantDto avisDto) {
        AvisRestaurant avis = AvisRestaurantDto.convertToEntity(avisDto, avisDto.getRestaurantId());
        return avisRestaurantService.updateAvisRestaurant(id, avis)
                .map(updatedAvis -> ResponseEntity.ok(AvisRestaurantDto.convertToDto(updatedAvis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/avisrestaurants/{id}")
    public ResponseEntity<Void> deleteAvisRestaurant(@PathVariable Long id) {
        return avisRestaurantService.deleteAvisRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
