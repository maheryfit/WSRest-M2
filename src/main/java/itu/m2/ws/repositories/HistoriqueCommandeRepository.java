package itu.m2.ws.repositories;

import itu.m2.ws.models.HistoriqueCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriqueCommandeRepository extends JpaRepository<HistoriqueCommande, Long> {
}
