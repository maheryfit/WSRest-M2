package itu.m2.ws.repositories;

import itu.m2.ws.models.AvisRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvisRestaurantRepository extends JpaRepository<AvisRestaurant, Long> {
    List<AvisRestaurant> findByRestaurantId(Long restaurantId);
}
