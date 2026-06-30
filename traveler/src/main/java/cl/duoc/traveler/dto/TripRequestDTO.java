package cl.duoc.traveler.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class TripRequestDTO {

    @NotNull(message = "El usuario es obligatorio")
    private UUID userId;

    @NotNull(message = "El destino es obligatorio")
    private UUID destinationId;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha de término es obligatoria")
    private LocalDate endDate;

    @NotNull(message = "El tipo de viaje es obligatorio")
    @Size(min = 3, max = 50, message = "El tipo de viaje debe tener entre 3 y 50 caracteres")
    private String travelType;

    private Integer companions;

    private String notes;
}
