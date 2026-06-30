package cl.duoc.destination.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationCategoryRequestDTO {

    @NotNull(message = "El ID del destino es obligatorio")
    private UUID destinationId;

    @NotNull(message = "El ID de la categoría es obligatorio")
    private UUID categoryId;
}
