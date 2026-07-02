package cl.duoc.review.controller;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewRequestDTO;
import cl.duoc.review.dto.ReviewResponseDTO;
import cl.duoc.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Review Controller", description = "Gestión de reseñas de destinos")
@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Crear reseña", description = "Requiere token JWT y destino existente")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ReviewRequestDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        ApiResponse<ReviewResponseDTO> response = reviewService.createReview(token, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reseña por ID")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> getReviewById(@PathVariable Long id) {
        ApiResponse<ReviewResponseDTO> response = reviewService.getReviewById(id);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar reseñas por destino", description = "Usa query param destinationId")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getReviewsByDestination(
            @RequestParam UUID destinationId) {
        ApiResponse<List<ReviewResponseDTO>> response = reviewService.getReviewsByDestination(destinationId);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar reseña", description = "Solo el autor puede modificar")
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> updateReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDTO dto) {
        String token = authHeader.replace("Bearer ", "");
        ApiResponse<ReviewResponseDTO> response = reviewService.updateReview(token, id, dto);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar reseña", description = "Solo el autor puede eliminar")
    public ResponseEntity<ApiResponse<Void>> deleteReview(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long id) {
        String token = authHeader.replace("Bearer ", "");
        ApiResponse<Void> response = reviewService.deleteReview(token, id);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}