package cl.duoc.traveler.controller;

import cl.duoc.traveler.dto.ApiResponse;
import cl.duoc.traveler.dto.TripDetailsRequestDTO;
import cl.duoc.traveler.dto.TripDetailsResponseDTO;
import cl.duoc.traveler.service.TripDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Trip Details Controller", description = "Endpoints para gestión de detalles de viajes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/traveler/trip-details")
public class TripDetailsController {

    private final TripDetailsService tripDetailsService;

    @PostMapping
    @Operation(summary = "Agregar detalle a viaje", description = "Agrega un detalle a un viaje validando token")
    public ResponseEntity<ApiResponse<TripDetailsResponseDTO>> addDetail(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TripDetailsRequestDTO dto) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<TripDetailsResponseDTO> response = tripDetailsService.addDetail(token, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "Listar detalles de un viaje", description = "Obtiene todos los detalles de un viaje validando token")
    public ResponseEntity<ApiResponse<List<TripDetailsResponseDTO>>> getDetailsByTrip(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID tripId) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<List<TripDetailsResponseDTO>> response = tripDetailsService.getDetailsByTrip(token, tripId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
