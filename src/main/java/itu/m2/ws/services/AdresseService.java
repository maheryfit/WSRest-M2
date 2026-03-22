package itu.m2.ws.services;

import itu.m2.ws.models.Adresse;
import itu.m2.ws.repositories.AdresseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdresseService {

    @Autowired
    private AdresseRepository adresseRepository;

    public List<Adresse> getAllAdresses() {
        return adresseRepository.findAll();
    }

    public Optional<Adresse> getAdresseById(Long id) {
        return adresseRepository.findById(id);
    }

    public Adresse createAdresse(Adresse adresse) {
        return adresseRepository.save(adresse);
    }

    public Optional<Adresse> updateAdresse(Long id, Adresse adresseDetails) {
        return adresseRepository.findById(id).map(adresse -> {
            adresse.setClient(adresseDetails.getClient());
            adresse.setLibelle(adresseDetails.getLibelle());
            adresse.setRue(adresseDetails.getRue());
            adresse.setVille(adresseDetails.getVille());
            adresse.setCodePostal(adresseDetails.getCodePostal());
            adresse.setLatitude(adresseDetails.getLatitude());
            adresse.setLongitude(adresseDetails.getLongitude());
            adresse.setParDefaut(adresseDetails.isParDefaut());
            return adresseRepository.save(adresse);
        });
    }

    public boolean deleteAdresse(Long id) {
        return adresseRepository.findById(id).map(adresse -> {
            adresseRepository.delete(adresse);
            return true;
        }).orElse(false);
    }
}
