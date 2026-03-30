package itu.m2.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommandationRestaurantDto {
    private Long id;
    private String nom;
    private String ville;
    private String adresse;
    private Double noteMoyenne;
}
