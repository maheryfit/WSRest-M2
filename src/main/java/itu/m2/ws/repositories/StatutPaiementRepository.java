package itu.m2.ws.repositories;

import itu.m2.ws.models.StatutPaiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutPaiementRepository extends JpaRepository<StatutPaiement, Long> {
    Optional<StatutPaiement> findByLibelle(String libelle);
}
