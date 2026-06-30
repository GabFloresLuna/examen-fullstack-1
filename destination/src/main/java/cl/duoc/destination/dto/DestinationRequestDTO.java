package cl.duoc.destination.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationRequestDTO {

    @NotBlank(message = "El nombre del destino no puede estar vacío")
    @Size(min = 3, max = 100, message = "El nombre del destino debe tener entre 3 y 100 caracteres")
    private String name;

    @NotBlank(message = "El país no puede estar vacío")
    @Size(min = 2, max = 100, message = "El país debe tener entre 2 y 100 caracteres")
    private String country;

    @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
    private String description;
}
