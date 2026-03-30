package itu.m2.ws.repositories;

import itu.m2.ws.models.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaiementRepository extends JpaRepository<Paiement, Long> {
    List<Paiement> findByCommandeId(Long commandeId);

    @Query("SELECT COALESCE(SUM(p.montant), 0) FROM Paiement p WHERE p.commande.id = :commandeId")
    double sumMontantByCommandeId(@Param("commandeId") Long commandeId);
}
