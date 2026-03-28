package itu.m2.ws.repositories;

import itu.m2.ws.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    @Query("SELECT r.id as id, r.nom as nom, r.noteMoyenne as noteMoyenne, r.ville as ville " +
           "FROM Restaurant r " +
           "WHERE r.ville IN (SELECT c.restaurant.ville FROM Commande c WHERE c.client.id = :clientId) " +
           "OR r.id NOT IN (SELECT c.restaurant.id FROM Commande c WHERE c.client.id = :clientId) " +
           "ORDER BY r.noteMoyenne DESC")
    List<Map<String, Object>> findRecommendationsByClientId(@Param("clientId") Long clientId);
}
