package itu.m2.ws.repositories;

import itu.m2.ws.dto.CommandesParJourDto;
import itu.m2.ws.models.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClientId(Long clientId);
    List<Commande> findByRestaurantId(Long restaurantId);

    @Query("SELECT new itu.m2.ws.dto.CommandesParJourDto(CAST(c.dateCreation AS java.sql.Date), COUNT(c.id)) " +
           "FROM Commande c " +
           "GROUP BY CAST(c.dateCreation AS java.sql.Date) " +
           "ORDER BY CAST(c.dateCreation AS java.sql.Date)")
    List<CommandesParJourDto> countCommandesByJour();
}
