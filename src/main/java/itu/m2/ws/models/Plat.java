package itu.m2.ws.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Table(name = "plats")
@Data @Entity @Builder @NoArgsConstructor @AllArgsConstructor
public class Plat {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    @PositiveOrZero
    private double prix;
    @Column(nullable = false)
    private String categorie;
    @Column(nullable = false)
    private boolean disponible;
    @PrePersist
    protected void onCreate() {
        this.disponible = true;
    }
}
