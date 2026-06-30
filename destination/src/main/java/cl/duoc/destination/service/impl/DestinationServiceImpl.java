package cl.duoc.destination.service.impl;

import cl.duoc.destination.dto.DestinationRequestDTO;
import cl.duoc.destination.dto.DestinationResponseDTO;
import cl.duoc.destination.model.Destination;
import cl.duoc.destination.repository.DestinationRepository;
import cl.duoc.destination.service.DestinationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;

    public DestinationServiceImpl(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    @Override
    public DestinationResponseDTO createDestination(DestinationRequestDTO request) {
        Destination destination = Destination.builder()
                .name(request.getName())
                .country(request.getCountry())
                .description(request.getDescription())
                .build();

        Destination saved = destinationRepository.save(destination);
        return toResponse(saved);
    }

    @Override
    public DestinationResponseDTO getDestinationById(UUID id) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));
        return toResponse(destination);
    }

    @Override
    public List<DestinationResponseDTO> getAllDestinations() {
        return destinationRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public DestinationResponseDTO updateDestination(UUID id, DestinationRequestDTO request) {
        Destination destination = destinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        destination.setName(request.getName());
        destination.setCountry(request.getCountry());
        destination.setDescription(request.getDescription());

        Destination updated = destinationRepository.save(destination);
        return toResponse(updated);
    }

    @Override
    public void deleteDestination(UUID id) {
        if (!destinationRepository.existsById(id)) {
            throw new RuntimeException("Destino no encontrado");
        }
        destinationRepository.deleteById(id);
    }
    
    @Override
    public boolean existsById(UUID id) {
        return destinationRepository.existsById(id);
    }

    private DestinationResponseDTO toResponse(Destination destination) {
        return DestinationResponseDTO.builder()
                .id(destination.getId())
                .name(destination.getName())
                .country(destination.getCountry())
                .description(destination.getDescription())
                .build();
    }
}
