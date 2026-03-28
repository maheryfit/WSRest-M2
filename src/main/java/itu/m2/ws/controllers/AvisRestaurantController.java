package itu.m2.ws.controllers;

import itu.m2.ws.dto.AvisRestaurantDto;
import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.services.AvisRestaurantService;
import itu.m2.ws.services.ClientService;
import itu.m2.ws.services.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AvisRestaurantController extends BaseController {

    @Autowired
    private AvisRestaurantService avisRestaurantService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CommandeService commandeService;

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
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AvisRestaurantDto> createAvisByRestaurantId(@PathVariable Long restaurantId, @Valid @RequestBody AvisRestaurantDto avisDto) {
        String email = getCurrentUserEmail();
        return clientService.getClientByEmail(email).map(client -> {
            // Verification: client must have at least one delivered order in this restaurant
            boolean hasDeliveredOrder = commandeService.getCommandesByClientId(client.getId()).stream()
                    .anyMatch(c -> Objects.equals(c.getRestaurant().getId(), restaurantId));
            
            if (!hasDeliveredOrder) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).<AvisRestaurantDto>build();
            }

            AvisRestaurant avis = AvisRestaurantDto.convertToEntity(avisDto, restaurantId);
            avis.setClient(client);
            return ResponseEntity.ok(AvisRestaurantDto.convertToDto(avisRestaurantService.createAvisRestaurant(avis)));
        }).orElse(ResponseEntity.notFound().build());
    }
    
    @PutMapping("/restaurants/{restaurantId}/avis/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<AvisRestaurantDto> updateAvisByRestaurantId(@PathVariable Long restaurantId, @PathVariable Long id, @Valid @RequestBody AvisRestaurantDto avisDto) {
        String email = getCurrentUserEmail();
        return avisRestaurantService.getAvisRestaurantById(id)
                .map(existingAvis -> {
                    if (!existingAvis.getClient().getUtilisateur().getEmail().equals(email)) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).<AvisRestaurantDto>build();
                    }
                    AvisRestaurant avisToUpdate = AvisRestaurantDto.convertToEntity(avisDto, restaurantId);
                    avisToUpdate.setClient(existingAvis.getClient());
                    return avisRestaurantService.updateAvisRestaurant(id, avisToUpdate)
                            .map(updatedAvis -> ResponseEntity.ok(AvisRestaurantDto.convertToDto(updatedAvis)))
                            .orElse(ResponseEntity.notFound().build());
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/restaurants/{restaurantId}/avis/{id}")
    @PreAuthorize("hasAnyRole('CLIENT', 'ADMIN')")
    public ResponseEntity<Void> deleteAvisByRestaurantId(@PathVariable Long restaurantId, @PathVariable Long id) {
        String email = getCurrentUserEmail();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        return avisRestaurantService.getAvisRestaurantById(id)
                .map(avis -> {
                    boolean isAuthor = avis.getClient().getUtilisateur().getEmail().equals(email);
                    if (isAuthor || isAdmin) {
                        return avisRestaurantService.deleteAvisRestaurant(id) ? 
                                ResponseEntity.ok().<Void>build() : ResponseEntity.notFound().<Void>build();
                    }
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).<Void>build();
                })
                .orElse(ResponseEntity.notFound().<Void>build());
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
        return avisRestaurantService.deleteAvisRestaurant(id) ? ResponseEntity.ok().<Void>build() : ResponseEntity.notFound().<Void>build();
    }
}
