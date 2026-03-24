package itu.m2.ws.repositories;

import itu.m2.ws.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByUtilisateurId(Long utilisateurId);
}
