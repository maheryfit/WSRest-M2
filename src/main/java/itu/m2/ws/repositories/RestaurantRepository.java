package itu.m2.ws.repositories;

import itu.m2.ws.models.Restaurant;
import itu.m2.ws.dto.TopRestaurantDto;
import itu.m2.ws.dto.RecommandationRestaurantDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    @Query("SELECT new itu.m2.ws.dto.TopRestaurantDto(r.id, r.nom, " +
           "COUNT(c.id), " +
           "r.noteMoyenne, " +
           "SUM(c.montantTotal)) " +
           "FROM Restaurant r " +
           "JOIN Commande c " +
           "WHERE c.statutCommande.id = 4 " + // 4 = Livrée/Terminée
           "AND (:fromDate IS NULL OR c.dateCreation >= :fromDate) " +
           "AND (:toDate IS NULL OR c.dateCreation <= :toDate) " +
           "GROUP BY r.id, r.nom, r.noteMoyenne " +
           "ORDER BY COUNT(c.id) DESC")
    List<TopRestaurantDto> findTopRestaurants(
            @Param("fromDate") Timestamp fromDate,
            @Param("toDate") Timestamp toDate);

    @Query("SELECT new itu.m2.ws.dto.RecommandationRestaurantDto(r.id, r.nom, r.ville, r.adresse, r.noteMoyenne) " +
           "FROM Restaurant r " +
           "LEFT JOIN r.adresse a " +
           "ORDER BY r.noteMoyenne DESC")
    List<RecommandationRestaurantDto> findRecommandationsForClient(@Param("clientId") Long clientId);

    @Query("SELECT r FROM Restaurant r WHERE r.utilisateur.email = :email")
    Optional<Restaurant> findByUtilisateurEmail(@Param("email") String email);
}
