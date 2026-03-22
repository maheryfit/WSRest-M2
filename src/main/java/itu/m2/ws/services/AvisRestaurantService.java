package itu.m2.ws.services;

import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.repositories.AvisRestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AvisRestaurantService {

    @Autowired
    private AvisRestaurantRepository avisRestaurantRepository;

    public List<AvisRestaurant> getAllAvisRestaurants() {
        return avisRestaurantRepository.findAll();
    }

    public Optional<AvisRestaurant> getAvisRestaurantById(Long id) {
        return avisRestaurantRepository.findById(id);
    }

    public AvisRestaurant createAvisRestaurant(AvisRestaurant avisRestaurant) {
        return avisRestaurantRepository.save(avisRestaurant);
    }

    public Optional<AvisRestaurant> updateAvisRestaurant(Long id, AvisRestaurant avisRestaurantDetails) {
        return avisRestaurantRepository.findById(id).map(avisRestaurant -> {
            avisRestaurant.setRestaurant(avisRestaurantDetails.getRestaurant());
            avisRestaurant.setClient(avisRestaurantDetails.getClient());
            avisRestaurant.setNote(avisRestaurantDetails.getNote());
            avisRestaurant.setCommentaire(avisRestaurantDetails.getCommentaire());
            avisRestaurant.setDateCreation(avisRestaurantDetails.getDateCreation());
            return avisRestaurantRepository.save(avisRestaurant);
        });
    }

    public boolean deleteAvisRestaurant(Long id) {
        return avisRestaurantRepository.findById(id).map(avisRestaurant -> {
            avisRestaurantRepository.delete(avisRestaurant);
            return true;
        }).orElse(false);
    }
}
