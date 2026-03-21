package itu.m2.ws.controllers;

import itu.m2.ws.dto.AvisRestaurantDto;
import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.models.Client;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.services.AvisRestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/avisrestaurants")
public class AvisRestaurantController {

    @Autowired
    private AvisRestaurantService avisRestaurantService;

    private AvisRestaurantDto convertToDto(AvisRestaurant avis) {
        return new AvisRestaurantDto(
                avis.getId(),
                avis.getRestaurant().getId(),
                avis.getClient().getId(),
                avis.getNote(),
                avis.getCommentaire(),
                avis.getDateCreation()
        );
    }

    private AvisRestaurant convertToEntity(AvisRestaurantDto avisDto) {
        AvisRestaurant avis = new AvisRestaurant();
        avis.setNote(avisDto.getNote());
        avis.setCommentaire(avisDto.getCommentaire());

        Restaurant restaurant = new Restaurant();
        restaurant.setId(avisDto.getRestaurantId());
        avis.setRestaurant(restaurant);

        Client client = new Client();
        client.setId(avisDto.getClientId());
        avis.setClient(client);

        return avis;
    }

    @GetMapping
    public List<AvisRestaurantDto> getAllAvisRestaurants() {
        return avisRestaurantService.getAllAvisRestaurants().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AvisRestaurantDto> getAvisRestaurantById(@PathVariable Long id) {
        return avisRestaurantService.getAvisRestaurantById(id)
                .map(avis -> ResponseEntity.ok(convertToDto(avis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public AvisRestaurantDto createAvisRestaurant(@Valid @RequestBody AvisRestaurantDto avisDto) {
        AvisRestaurant avis = convertToEntity(avisDto);
        return convertToDto(avisRestaurantService.createAvisRestaurant(avis));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AvisRestaurantDto> updateAvisRestaurant(@PathVariable Long id, @Valid @RequestBody AvisRestaurantDto avisDto) {
        AvisRestaurant avis = convertToEntity(avisDto);
        return avisRestaurantService.updateAvisRestaurant(id, avis)
                .map(updatedAvis -> ResponseEntity.ok(convertToDto(updatedAvis)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAvisRestaurant(@PathVariable Long id) {
        return avisRestaurantService.deleteAvisRestaurant(id) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
