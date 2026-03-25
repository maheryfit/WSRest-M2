package itu.m2.ws.repositories;

import itu.m2.ws.models.Livraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long> {
    java.util.List<Livraison> findByLivreurId(Long livreurId);

    java.util.Optional<Livraison> findByCommandeId(Long commandeId);
}
