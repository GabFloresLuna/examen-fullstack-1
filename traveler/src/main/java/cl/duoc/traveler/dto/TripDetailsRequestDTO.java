package cl.duoc.traveler.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.util.UUID;

@Data
public class TripDetailsRequestDTO {

    @NotNull(message = "El viaje es obligatorio")
    private UUID tripId;

    @NotBlank(message = "La clave del detalle no puede estar vacía")
    @Size(min = 3, max = 100, message = "La clave debe tener entre 3 y 100 caracteres")
    private String detailKey;

    @NotBlank(message = "El valor del detalle no puede estar vacío")
    @Size(min = 1, max = 255, message = "El valor debe tener entre 1 y 255 caracteres")
    private String detailValue;
}
