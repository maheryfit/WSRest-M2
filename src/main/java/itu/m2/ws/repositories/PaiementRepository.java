package itu.m2.ws.repositories;

import itu.m2.ws.models.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    Optional<Paiement> findByCommandeId(Long commandeId);
}
