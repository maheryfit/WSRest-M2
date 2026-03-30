package itu.m2.ws.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import itu.m2.ws.models.AvisRestaurant;
import itu.m2.ws.repositories.AvisRestaurantRepository;
import itu.m2.ws.repositories.RestaurantRepository;

@Service
public class AvisRestaurantService {

    @Autowired
    private AvisRestaurantRepository avisRestaurantRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<AvisRestaurant> getAllAvisRestaurants() {
        return avisRestaurantRepository.findAll();
    }

    public List<AvisRestaurant> getAvisByRestaurantId(Long restaurantId) {
        return avisRestaurantRepository.findByRestaurantId(restaurantId);
    }

    public Optional<AvisRestaurant> getAvisRestaurantById(Long id) {
        return avisRestaurantRepository.findById(id);
    }

    @Transactional
    public AvisRestaurant createAvisRestaurant(AvisRestaurant avisRestaurant) {
        AvisRestaurant savedAvis = avisRestaurantRepository.save(avisRestaurant);
        updateRestaurantNoteMoyenne(savedAvis.getRestaurant().getId());
        return savedAvis;
    }

    @Transactional
    public Optional<AvisRestaurant> updateAvisRestaurant(Long id, AvisRestaurant avisRestaurantDetails) {
        return avisRestaurantRepository.findById(id).map(avisRestaurant -> {
            avisRestaurant.setRestaurant(avisRestaurantDetails.getRestaurant());
            avisRestaurant.setClient(avisRestaurantDetails.getClient());
            avisRestaurant.setNote(avisRestaurantDetails.getNote());
            avisRestaurant.setCommentaire(avisRestaurantDetails.getCommentaire());
            avisRestaurant.setDateCreation(avisRestaurantDetails.getDateCreation());

            AvisRestaurant updatedAvis = avisRestaurantRepository.save(avisRestaurant);
            updateRestaurantNoteMoyenne(updatedAvis.getRestaurant().getId());
            return updatedAvis;
        });
    }

    @Transactional
    public boolean deleteAvisRestaurant(Long id) {
        return avisRestaurantRepository.findById(id).map(avisRestaurant -> {
            Long restaurantId = avisRestaurant.getRestaurant().getId();
            avisRestaurantRepository.delete(avisRestaurant);
            updateRestaurantNoteMoyenne(restaurantId);
            return true;
        }).orElse(false);
    }

    private void updateRestaurantNoteMoyenne(Long restaurantId) {
        Double averageNote = avisRestaurantRepository.calculateAverageNoteByRestaurantId(restaurantId);
        restaurantRepository.findById(restaurantId).ifPresent(restaurant -> {
            restaurant.setNoteMoyenne(averageNote != null ? averageNote : 0.0);
            restaurantRepository.save(restaurant);
        });
    }
}
