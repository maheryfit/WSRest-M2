package itu.m2.ws.repositories;

import itu.m2.ws.models.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    @Query("SELECT l.livreur.id as livreurId, l.livreur.nom as nom, COUNT(l) as totalLivraisons, " +
           "AVG(TIMESTAMPDIFF(MINUTE, l.dateCreation, l.dateLivraisonReelle)) as tempsMoyenMinutes, " +
           "SUM(CASE WHEN l.dateLivraisonReelle > l.dateLivraisonEstimee THEN 1 ELSE 0 END) * 1.0 / COUNT(l) as tauxRetard " +
           "FROM Livraison l " +
           "WHERE l.dateLivraisonReelle IS NOT NULL " +
           "GROUP BY l.livreur.id, l.livreur.nom " +
           "ORDER BY COUNT(l) DESC")
    List<Map<String, Object>> findLivreursPerformance();
    
    Optional<Livraison> findByCommandeId(Long commandeId);
    List<Livraison> findByLivreurId(Long livreurId);
}
