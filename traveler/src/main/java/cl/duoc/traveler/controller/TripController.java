package cl.duoc.traveler.controller;

import cl.duoc.traveler.dto.ApiResponse;
import cl.duoc.traveler.dto.TripRequestDTO;
import cl.duoc.traveler.dto.TripResponseDTO;
import cl.duoc.traveler.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Trip Controller", description = "Endpoints para gestión de viajes")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/traveler/trips")
public class TripController {

    private final TripService tripService;

    @PostMapping
    @Operation(summary = "Crear viaje", description = "Crea un nuevo viaje validando token y destino")
    public ResponseEntity<ApiResponse<TripResponseDTO>> createTrip(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody TripRequestDTO dto) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<TripResponseDTO> response = tripService.createTrip(token, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/user")
    @Operation(summary = "Listar viajes del usuario autenticado", description = "Obtiene todos los viajes del usuario validado por token")
    public ResponseEntity<ApiResponse<List<TripResponseDTO>>> getTripsByUser(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<List<TripResponseDTO>> response = tripService.getTripsByUser(token);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}
