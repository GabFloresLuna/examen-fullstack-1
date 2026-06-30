package cl.duoc.destination.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationCategoryResponseDTO {
    private UUID id;
    private UUID destinationId;
    private UUID categoryId;
}
