package itu.m2.ws.models;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clients", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"nom", "prenom"})
})
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    @Column(nullable = false)
    private String nom;
    @Column(nullable = false)
    private String prenom;
    @Column(nullable = false, unique = true)
    private String telephone;
}
