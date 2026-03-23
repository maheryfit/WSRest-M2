package itu.m2.ws.repositories;

import itu.m2.ws.models.Plat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlatRepository extends JpaRepository<Plat, Long> {
    List<Plat> findByRestaurantId(Long restaurantId);
    Optional<Plat> findByIdAndRestaurantId(Long id, Long restaurantId);
}
