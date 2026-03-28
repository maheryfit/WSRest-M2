package itu.m2.ws.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itu.m2.ws.models.AvisRestaurant;

@Repository
public interface AvisRestaurantRepository extends JpaRepository<AvisRestaurant, Long> {
    List<AvisRestaurant> findByRestaurantId(Long restaurantId);
}
