package itu.m2.ws.services;

import itu.m2.ws.repositories.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private CommandeRepository commandeRepository;

    public List<Map<String, Object>> getTopRestaurants(String fromStr, String toStr) {
        Timestamp from = (fromStr != null && !fromStr.isEmpty())
                ? Timestamp.valueOf(LocalDate.parse(fromStr).atStartOfDay())
                : null;
        Timestamp to = (toStr != null && !toStr.isEmpty())
                ? Timestamp.valueOf(LocalDate.parse(toStr).atTime(LocalTime.MAX))
                : null;
        return commandeRepository.findTopRestaurants(from, to);
    }

    public List<Map<String, Object>> getMeilleursClients() {
        return commandeRepository.findMeilleursClients();
    }

    public List<Map<String, Object>> getCommandesParJour() {
        return commandeRepository.findCommandesParJour();
    }

}
