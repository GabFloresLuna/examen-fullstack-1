package cl.duoc.destination.controller;

import cl.duoc.destination.dto.*;
import cl.duoc.destination.service.DestinationService;
import cl.duoc.destination.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Destinations Controller", description = "Endpoints para gestión de destinos turísticos.")
@RestController
@RequestMapping("/api/v1/destination/destinations")
public class DestinationController {

    private final DestinationService destinationService;
    private final AuthService authService;

    public DestinationController(DestinationService destinationService, AuthService authService) {
        this.destinationService = destinationService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Crear destino", description = "Permite registrar un nuevo destino en el sistema.")
    public ResponseEntity<ApiResponse<DestinationResponseDTO>> createDestination(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody DestinationRequestDTO request) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Token inválido", null));
        }

        DestinationResponseDTO response = destinationService.createDestination(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Destino creado correctamente", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener destino por ID", description = "Permite consultar un destino específico mediante su identificador único.")
    public ResponseEntity<ApiResponse<DestinationResponseDTO>> getDestinationById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Token inválido", null));
        }

        DestinationResponseDTO response = destinationService.getDestinationById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Destino encontrado", response));
    }

    @GetMapping
    @Operation(summary = "Listar destinos", description = "Permite obtener un listado de todos los destinos registrados en el sistema.")
    public ResponseEntity<ApiResponse<List<DestinationResponseDTO>>> getAllDestinations(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Token inválido", null));
        }

        List<DestinationResponseDTO> response = destinationService.getAllDestinations();
        return ResponseEntity.ok(new ApiResponse<>(200, "Listado de destinos", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar destino", description = "Permite modificar los datos de un destino existente.")
    public ResponseEntity<ApiResponse<DestinationResponseDTO>> updateDestination(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id,
            @Valid @RequestBody DestinationRequestDTO request) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Token inválido", null));
        }

        DestinationResponseDTO response = destinationService.updateDestination(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Destino actualizado correctamente", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar destino", description = "Permite eliminar un destino existente mediante su identificador único.")
    public ResponseEntity<ApiResponse<Void>> deleteDestination(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401).body(new ApiResponse<>(401, "Token inválido", null));
        }

        destinationService.deleteDestination(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Destino eliminado correctamente", null));
    }

    @GetMapping("/exists")
    @Operation(summary = "Validar destino", description = "Permite validar si un destino existe en el sistema.")
    public ResponseEntity<ApiResponse<Boolean>> exists(@RequestParam UUID id) {
        boolean exists = destinationService.existsById(id);

        if (exists) {
            return ResponseEntity.ok(new ApiResponse<>(200, "Destino válido", true));
        } else {
            return ResponseEntity.status(404).body(new ApiResponse<>(404, "Destino no encontrado", false));
        }
    }
}
