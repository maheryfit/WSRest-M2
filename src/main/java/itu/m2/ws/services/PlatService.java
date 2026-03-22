package itu.m2.ws.services;

import itu.m2.ws.models.Plat;
import itu.m2.ws.repositories.PlatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlatService {

    @Autowired
    private PlatRepository platRepository;

    public List<Plat> getAllPlats() {
        return platRepository.findAll();
    }

    public Optional<Plat> getPlatById(Long id) {
        return platRepository.findById(id);
    }

    public Plat createPlat(Plat plat) {
        return platRepository.save(plat);
    }

    public Optional<Plat> updatePlat(Long id, Plat platDetails) {
        return platRepository.findById(id).map(plat -> {
            plat.setRestaurant(platDetails.getRestaurant());
            plat.setNom(platDetails.getNom());
            plat.setDescription(platDetails.getDescription());
            plat.setPrix(platDetails.getPrix());
            plat.setCategorie(platDetails.getCategorie());
            plat.setDisponible(platDetails.isDisponible());
            return platRepository.save(plat);
        });
    }

    public boolean deletePlat(Long id) {
        return platRepository.findById(id).map(plat -> {
            platRepository.delete(plat);
            return true;
        }).orElse(false);
    }
}
