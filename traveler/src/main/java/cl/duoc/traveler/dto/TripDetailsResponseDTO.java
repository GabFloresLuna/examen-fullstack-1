package cl.duoc.traveler.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class TripDetailsResponseDTO {
    private UUID id;
    private UUID tripId;
    private String detailKey;
    private String detailValue;
}