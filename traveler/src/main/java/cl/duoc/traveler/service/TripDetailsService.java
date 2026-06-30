package cl.duoc.traveler.service;

import cl.duoc.traveler.dto.ApiResponse;
import cl.duoc.traveler.dto.TripDetailsRequestDTO;
import cl.duoc.traveler.dto.TripDetailsResponseDTO;
import cl.duoc.traveler.dto.UserDTO;
import cl.duoc.traveler.model.Trip;
import cl.duoc.traveler.model.TripDetails;
import cl.duoc.traveler.repository.TripDetailsRepository;
import cl.duoc.traveler.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripDetailsService {

    private final TripDetailsRepository tripDetailsRepository;
    private final TripRepository tripRepository;
    private final AuthService authService;

    // Agregar detalle a un viaje validando token
    public ApiResponse<TripDetailsResponseDTO> addDetail(String token, TripDetailsRequestDTO dto) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();

        Trip trip = tripRepository.findById(dto.getTripId())
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        if (!trip.getUserId().equals(user.getId())) {
            return new ApiResponse<>(403, "Usuario no autorizado para modificar este viaje", null);
        }

        TripDetails detail = new TripDetails();
        detail.setTrip(trip);
        detail.setDetailKey(dto.getDetailKey());
        detail.setDetailValue(dto.getDetailValue());

        TripDetails saved = tripDetailsRepository.save(detail);

        TripDetailsResponseDTO responseDTO = new TripDetailsResponseDTO(
                saved.getId(),
                saved.getTrip().getId(),
                saved.getDetailKey(),
                saved.getDetailValue()
        );

        return new ApiResponse<>(200, "Detalle agregado correctamente", responseDTO);
    }

    // Listar detalles de un viaje validando token
    public ApiResponse<List<TripDetailsResponseDTO>> getDetailsByTrip(String token, UUID tripId) {
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new IllegalArgumentException("Viaje no encontrado"));

        if (!trip.getUserId().equals(user.getId())) {
            return new ApiResponse<>(403, "Usuario no autorizado para ver este viaje", null);
        }

        List<TripDetailsResponseDTO> details = tripDetailsRepository.findByTrip(trip)
                .stream()
                .map(detail -> new TripDetailsResponseDTO(
                        detail.getId(),
                        detail.getTrip().getId(),
                        detail.getDetailKey(),
                        detail.getDetailValue()
                ))
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Listado de detalles", details);
    }
}
