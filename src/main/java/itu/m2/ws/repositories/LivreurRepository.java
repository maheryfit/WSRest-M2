package itu.m2.ws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import itu.m2.ws.models.Livreur;
import itu.m2.ws.dto.PerformanceLivreurDto;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    Optional<Livreur> findByUtilisateurId(Long utilisateurId);

    @Query("SELECT new itu.m2.ws.dto.PerformanceLivreurDto(l.id, l.nom, " +
           "COUNT(liv.id), " +
           "AVG(TIMESTAMPDIFF(MINUTE, liv.dateAffectation, liv.dateLivraisonReelle)), " +
           "CASE WHEN COUNT(liv.id) > 0 THEN (SUM(CASE WHEN liv.dateLivraisonReelle > liv.dateLivraisonPrevue THEN 1 ELSE 0 END) * 100.0) / COUNT(liv.id) ELSE 0.0 END) " +
           "FROM Livreur l " +
           "JOIN l.livraisons liv " +
           "WHERE liv.statutLivraison.id = 4 " + // 4 = Livrée
           "GROUP BY l.id, l.nom " +
           "ORDER BY COUNT(liv.id) DESC")
    List<PerformanceLivreurDto> getLivreursPerformance();
}
