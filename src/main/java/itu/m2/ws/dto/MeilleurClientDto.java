package itu.m2.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeilleurClientDto {
    private Long id;
    private String nom;
    private Double totalDepense;
    private Long nombreCommandes;
    private Double panierMoyen;
}
