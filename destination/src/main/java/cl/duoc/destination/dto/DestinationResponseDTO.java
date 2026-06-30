package cl.duoc.destination.dto;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DestinationResponseDTO {
    private UUID id;
    private String name;
    private String country;
    private String description;
}
