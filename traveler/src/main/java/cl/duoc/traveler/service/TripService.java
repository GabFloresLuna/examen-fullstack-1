package cl.duoc.traveler.service;

import cl.duoc.traveler.dto.ApiResponse;
import cl.duoc.traveler.dto.TripRequestDTO;
import cl.duoc.traveler.dto.TripResponseDTO;
import cl.duoc.traveler.dto.UserDTO;
import cl.duoc.traveler.model.Trip;
import cl.duoc.traveler.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final AuthService authService;
    private final DestinationService destinationService;

    // Crear un nuevo viaje validando usuario y destino
    public ApiResponse<TripResponseDTO> createTrip(String token, TripRequestDTO dto) {
        // Validar token contra Login Service
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();

        // Validar destino contra Destination Service pasando el token
        ApiResponse<Boolean> destResponse = destinationService.validateDestination(dto.getDestinationId(), token);
        if (destResponse == null || destResponse.getCode() != 200 || destResponse.getData() == null || !destResponse.getData()) {
            return new ApiResponse<>(400, "Destino inválido", null);
        }

        // Crear viaje
        Trip trip = new Trip();
        trip.setUserId(user.getId()); // userId obtenido del token
        trip.setDestinationId(dto.getDestinationId());
        trip.setStartDate(dto.getStartDate());
        trip.setEndDate(dto.getEndDate());
        trip.setTravelType(dto.getTravelType());
        trip.setCompanions(dto.getCompanions());
        trip.setNotes(dto.getNotes());

        Trip saved = tripRepository.save(trip);

        TripResponseDTO responseDTO = new TripResponseDTO(
                saved.getId(),
                saved.getUserId(),
                saved.getDestinationId(),
                saved.getStartDate(),
                saved.getEndDate(),
                saved.getTravelType(),
                saved.getCompanions(),
                saved.getNotes()
        );

        return new ApiResponse<>(200, "Viaje creado correctamente", responseDTO);
    }

    // Listar viajes del usuario autenticado
    public ApiResponse<List<TripResponseDTO>> getTripsByUser(String token) {
        // Validar token contra Login Service
        ApiResponse<UserDTO> authResponse = authService.validateToken(token);
        if (authResponse.getCode() != 200 || authResponse.getData() == null) {
            return new ApiResponse<>(401, "Token inválido", null);
        }

        UserDTO user = authResponse.getData();

        // Buscar viajes del usuario autenticado
        List<TripResponseDTO> trips = tripRepository.findByUserId(user.getId())
                .stream()
                .map(trip -> new TripResponseDTO(
                        trip.getId(),
                        trip.getUserId(),
                        trip.getDestinationId(),
                        trip.getStartDate(),
                        trip.getEndDate(),
                        trip.getTravelType(),
                        trip.getCompanions(),
                        trip.getNotes()
                ))
                .collect(Collectors.toList());

        return new ApiResponse<>(200, "Listado de viajes", trips);
    }
}
