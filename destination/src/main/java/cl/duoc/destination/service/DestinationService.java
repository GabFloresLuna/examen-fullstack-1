package cl.duoc.destination.service;

import cl.duoc.destination.dto.DestinationRequestDTO;
import cl.duoc.destination.dto.DestinationResponseDTO;
import java.util.List;
import java.util.UUID;

public interface DestinationService {
    DestinationResponseDTO createDestination(DestinationRequestDTO request);
    DestinationResponseDTO getDestinationById(UUID id);
    List<DestinationResponseDTO> getAllDestinations();
    DestinationResponseDTO updateDestination(UUID id, DestinationRequestDTO request);
    void deleteDestination(UUID id);
    boolean existsById(UUID id);
}
