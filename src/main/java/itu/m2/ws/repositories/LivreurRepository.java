package itu.m2.ws.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import itu.m2.ws.models.Livreur;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {
    java.util.Optional<Livreur> findByUtilisateurId(Long utilisateurId);

    java.util.Optional<Livreur> findByUtilisateurEmail(String email);
    
    java.util.Optional<Livreur> findByLivreurId(Long livreurId);
}
