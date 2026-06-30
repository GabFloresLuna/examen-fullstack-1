package cl.duoc.traveler.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID userId; // FK hacia Login Service

    @Column(name = "destination_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID destinationId; // FK hacia Destination Service

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "travel_type", nullable = false, length = 50)
    private String travelType; // Ej: "Business", "Leisure"

    @Column(name = "companions")
    private Integer companions; // Número de acompañantes

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes; // Observaciones opcionales

    @PrePersist
    public void generateId() {
        if (id == null) id = UUID.randomUUID();
    }
}
