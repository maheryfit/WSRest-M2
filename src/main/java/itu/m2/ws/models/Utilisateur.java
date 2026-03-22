package itu.m2.ws.models;

import itu.m2.ws.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "utilisateurs")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String motDePasseHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean actif;

    @Column(name = "date_creation", updatable = false)
    private Timestamp dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = Timestamp.valueOf(LocalDateTime.now());
        this.actif = true;
    }
}