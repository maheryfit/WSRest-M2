package itu.m2.ws.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopRestaurantDto {
    private Long id;
    private String nom;
    private Long nombreCommandesLivrees;
    private Double noteMoyenne;
    private Double chiffreAffaires;
}
