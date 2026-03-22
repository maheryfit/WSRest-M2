package itu.m2.ws.repositories;

import itu.m2.ws.models.StatutCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatutCommandeRepository extends JpaRepository<StatutCommande, Long> {
}
