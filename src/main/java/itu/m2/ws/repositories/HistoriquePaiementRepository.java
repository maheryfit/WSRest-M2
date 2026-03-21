package itu.m2.ws.repositories;

import itu.m2.ws.models.HistoriquePaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriquePaiementRepository extends JpaRepository<HistoriquePaiement, Long> {
}
