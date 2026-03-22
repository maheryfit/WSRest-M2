package itu.m2.ws.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "avis_restaurants")
public class AvisRestaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int note;
    @Column(nullable = false)
    private String commentaire;
    @Column(nullable = false)
    private Timestamp dateCreation;

    @PrePersist
    protected void onCreate() {
        this.dateCreation = Timestamp.valueOf(LocalDateTime.now());
    }
}
