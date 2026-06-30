package cl.duoc.destination.service;

import cl.duoc.destination.dto.DestinationCategoryRequestDTO;
import cl.duoc.destination.dto.DestinationCategoryResponseDTO;

import java.util.List;
import java.util.UUID;

public interface DestinationCategoryService {
    DestinationCategoryResponseDTO createDestinationCategory(DestinationCategoryRequestDTO request);
    List<DestinationCategoryResponseDTO> getAllDestinationCategories();
    void deleteDestinationCategory(UUID id);
}
