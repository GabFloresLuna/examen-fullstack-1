package cl.duoc.traveler.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "trip_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TripDetails {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "trip_id", columnDefinition = "BINARY(16)")
    private Trip trip;

    @Column(name = "detail_key", nullable = false, length = 100)
    private String detailKey; // Ej: "Transport", "Budget", "Activity"

    @Column(name = "detail_value", nullable = false, length = 255)
    private String detailValue; // Ej: "Plane", "1500 USD", "Conference"

    @PrePersist
    public void generateId() {
        if (id == null) id = UUID.randomUUID();
    }
}
