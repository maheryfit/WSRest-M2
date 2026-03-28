package itu.m2.ws.repositories;

import itu.m2.ws.models.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);

    @Query("SELECT c.restaurant.id as restaurantId, c.restaurant.nom as nom, COUNT(c) as totalCommandes, " +
           "AVG(c.restaurant.noteMoyenne) as noteMoyenne, SUM(c.montantTotal) as chiffreAffaires " +
           "FROM Commande c " +
           "WHERE c.statutCommande.libelle = 'LIVREE' " +
           "AND (:from IS NULL OR c.dateCreation >= :from) " +
           "AND (:to IS NULL OR c.dateCreation <= :to) " +
           "GROUP BY c.restaurant.id, c.restaurant.nom " +
           "ORDER BY COUNT(c) DESC")
    List<Map<String, Object>> findTopRestaurants(@Param("from") Timestamp from, @Param("to") Timestamp to);

    @Query("SELECT c.client.id as clientId, c.client.nom as nom, SUM(c.montantTotal) as totalDepense, " +
           "COUNT(c) as nombreCommandes, AVG(c.montantTotal) as panierMoyen " +
           "FROM Commande c " +
           "WHERE c.statutCommande.libelle = 'LIVREE' " +
           "GROUP BY c.client.id, c.client.nom " +
           "ORDER BY SUM(c.montantTotal) DESC")
    List<Map<String, Object>> findMeilleursClients();

    @Query("SELECT CAST(c.dateCreation AS date) as date_creation, COUNT(c) as nombreCommandes " +
           "FROM Commande c " +
           "GROUP BY CAST(c.dateCreation AS date) " +
           "ORDER BY CAST(c.dateCreation AS date) ASC")
    List<Map<String, Object>> findCommandesParJour();
}
