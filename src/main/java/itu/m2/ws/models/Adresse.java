package itu.m2.ws.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "adresses")
@Data @Entity @Builder @NoArgsConstructor @AllArgsConstructor
public class Adresse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Column(nullable = false)
    private String libelle;
    @Column(nullable = false)
    private String rue;
    @Column(nullable = false)
    private String ville;
    @Column(nullable = false)
    private String codePostal;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private boolean parDefaut;

    @PrePersist
    protected void onCreate() {
        this.parDefaut = true;
    }

}
