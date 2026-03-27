package itu.m2.ws.repositories;

import itu.m2.ws.models.Client;
import itu.m2.ws.dto.MeilleurClientDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUtilisateurId(Long utilisateurId);

    @Query("SELECT new itu.m2.ws.dto.MeilleurClientDto(c.id, c.nom, " +
           "SUM(com.montantTotal), " +
           "COUNT(com.id), " +
           "CASE WHEN COUNT(com.id) > 0 THEN SUM(com.montantTotal) / COUNT(com.id) ELSE 0.0 END) " +
           "FROM Client c " +
           "JOIN c.commandes com " +
           "WHERE com.statutCommande.id = 4 " + // 4 = Livrée/Terminée
           "GROUP BY c.id, c.nom " +
           "ORDER BY SUM(com.montantTotal) DESC")
    List<MeilleurClientDto> findMeilleursClients();
}
