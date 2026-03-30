package itu.m2.ws.services;

import itu.m2.ws.enums.Role;
import itu.m2.ws.models.Restaurant;
import itu.m2.ws.models.Utilisateur;
import itu.m2.ws.repositories.RestaurantRepository;
import itu.m2.ws.repositories.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }
    
    public Optional<Restaurant> getRestaurantByEmail(String email) {
        return restaurantRepository.findByUtilisateurEmail(email);
    }

    @Transactional
    public Restaurant createRestaurant(Restaurant restaurant) {
        Utilisateur savedUtilisateur = utilisateurService.createUtilisateur(restaurant.getUtilisateur());
        restaurant.setUtilisateur(savedUtilisateur);
        return restaurantRepository.save(restaurant);
    }

    @Transactional
    public Optional<Restaurant> updateRestaurant(Long id, Restaurant restaurantDetails) {
        return restaurantRepository.findById(id).map(restaurant -> {
            utilisateurService.updateUtilisateur(restaurantDetails.getUtilisateur(), Role.RESTAURANT);
            restaurant.setUtilisateur(restaurantDetails.getUtilisateur());
            restaurant.setNom(restaurantDetails.getNom());
            restaurant.setDescription(restaurantDetails.getDescription());
            restaurant.setTelephone(restaurantDetails.getTelephone());
            // restaurant.setAdresse(restaurantDetails.getAdresse());
            // restaurant.setVille(restaurantDetails.getVille());
            // restaurant.setLatitude(restaurantDetails.getLatitude());
            // restaurant.setLongitude(restaurantDetails.getLongitude());
            // restaurant.setOuvert(restaurantDetails.isOuvert());
            // restaurant.setNoteMoyenne(restaurantDetails.getNoteMoyenne());
            return restaurantRepository.save(restaurant);
        });
    }

    public boolean deleteRestaurant(Long id) {
        return restaurantRepository.findById(id).map(restaurant -> {
            restaurantRepository.delete(restaurant);
            utilisateurRepository.delete(restaurant.getUtilisateur());
            return true;
        }).orElse(false);
    }
}
