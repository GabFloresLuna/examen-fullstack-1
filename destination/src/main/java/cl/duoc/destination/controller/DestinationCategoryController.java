package cl.duoc.destination.controller;

import cl.duoc.destination.dto.*;
import cl.duoc.destination.service.DestinationCategoryService;
import cl.duoc.destination.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Destination Categories Controller", description = "Endpoints para gestión de relaciones destino-categoría.")
@RestController
@RequestMapping("/api/v1/destination/destination-categories")
public class DestinationCategoryController {

    private final DestinationCategoryService destinationCategoryService;
    private final AuthService authService;

    public DestinationCategoryController(DestinationCategoryService destinationCategoryService,
                                         AuthService authService) {
        this.destinationCategoryService = destinationCategoryService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Crear relación destino-categoría", description = "Permite asociar un destino con una categoría.")
    public ResponseEntity<ApiResponse<DestinationCategoryResponseDTO>> createDestinationCategory(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody DestinationCategoryRequestDTO request) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        DestinationCategoryResponseDTO response = destinationCategoryService.createDestinationCategory(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Relación destino-categoría creada correctamente", response));
    }

    @GetMapping
    @Operation(summary = "Listar relaciones destino-categoría", description = "Permite obtener un listado de todas las asociaciones entre destinos y categorías.")
    public ResponseEntity<ApiResponse<List<DestinationCategoryResponseDTO>>> getAllDestinationCategories(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        List<DestinationCategoryResponseDTO> response = destinationCategoryService.getAllDestinationCategories();
        return ResponseEntity.ok(new ApiResponse<>(200, "Listado de relaciones destino-categoría", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar relación destino-categoría", description = "Permite eliminar una asociación entre un destino y una categoría mediante su identificador único.")
    public ResponseEntity<ApiResponse<Void>> deleteDestinationCategory(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        destinationCategoryService.deleteDestinationCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Relación destino-categoría eliminada correctamente", null));
    }
}
