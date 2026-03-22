package itu.m2.ws.repositories;

import itu.m2.ws.models.StatutLivraison;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutLivraisonRepository extends JpaRepository<StatutLivraison, Long> {
}
