package itu.m2.ws.services;

import itu.m2.ws.models.Restaurant;
import itu.m2.ws.repositories.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Optional<Restaurant> getRestaurantById(Long id) {
        return restaurantRepository.findById(id);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    public Optional<Restaurant> updateRestaurant(Long id, Restaurant restaurantDetails) {
        return restaurantRepository.findById(id).map(restaurant -> {
            restaurant.setUtilisateur(restaurantDetails.getUtilisateur());
            restaurant.setNom(restaurantDetails.getNom());
            restaurant.setDescription(restaurantDetails.getDescription());
            restaurant.setTelephone(restaurantDetails.getTelephone());
            restaurant.setAdresse(restaurantDetails.getAdresse());
            restaurant.setVille(restaurantDetails.getVille());
            restaurant.setLatitude(restaurantDetails.getLatitude());
            restaurant.setLongitude(restaurantDetails.getLongitude());
            restaurant.setOuvert(restaurantDetails.isOuvert());
            restaurant.setNoteMoyenne(restaurantDetails.getNoteMoyenne());
            return restaurantRepository.save(restaurant);
        });
    }

    public boolean deleteRestaurant(Long id) {
        return restaurantRepository.findById(id).map(restaurant -> {
            restaurantRepository.delete(restaurant);
            return true;
        }).orElse(false);
    }
}
