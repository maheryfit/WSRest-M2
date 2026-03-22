package itu.m2.ws.models;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Table(name = "restaurants", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"adresse", "ville"})
})
@Data @Entity @Builder @NoArgsConstructor @AllArgsConstructor
public class Restaurant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;
    @Column(nullable = false, unique = true)
    private String nom;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false, unique = true)
    private String telephone;
    @Column(nullable = false)
    private String adresse;
    @Column(nullable = false)
    private String ville;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private boolean ouvert;
    @Column(nullable = false)
    @PositiveOrZero
    private double noteMoyenne;
}
