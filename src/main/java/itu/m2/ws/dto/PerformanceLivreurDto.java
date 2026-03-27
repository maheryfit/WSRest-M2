package itu.m2.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PerformanceLivreurDto {
    private Long id;
    private String nom;
    private Long nombreLivraisons;
    private Double tempsMoyenLivraison; // en minutes par exemple
    private Double tauxRetard; // pourcentage
}
