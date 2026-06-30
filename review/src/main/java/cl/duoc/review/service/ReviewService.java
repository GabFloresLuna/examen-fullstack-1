package cl.duoc.review.service;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewRequestDTO;
import cl.duoc.review.dto.ReviewResponseDTO;
import cl.duoc.review.dto.UserDTO;
import cl.duoc.review.model.Review;
import cl.duoc.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final WebClient.Builder webClientBuilder;

    // ==================== VALIDACIÓN DE TOKEN ====================

    private ApiResponse<UserDTO> validateToken(String token) {
        try {
            log.debug("Validando token contra Login Service...");
            return webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("login")
                            .path("/api/v1/users/validate")
                            .queryParam("token", token)
                            .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserDTO>>() {})
                    .block();
        } catch (WebClientResponseException e) {
            log.error("Error HTTP al validar token: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return new ApiResponse<>(e.getStatusCode().value(), "Error al validar token: " + e.getMessage(), null);
        } catch (Exception e) {
            log.error("Error inesperado al validar token: {}", e.getMessage(), e);
            return new ApiResponse<>(500, "Error al validar token: " + e.getMessage(), null);
        }
    }

    // ==================== VALIDACIÓN DE DESTINO ====================

    private boolean destinationExists(UUID destinationId, String token) {
        try {
            log.debug("Validando existencia del destino {} contra Destination Service...", destinationId);
            ApiResponse<Boolean> response = webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .scheme("http")
                            .host("destination")
                            .path("/api/v1/destination/destinations/exists")
                            .queryParam("id", destinationId)
                            .build())
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<Boolean>>() {})
                    .block();

            if (response == null) {
                log.warn("Respuesta nula al validar destino {}", destinationId);
                return false;
            }
            boolean exists = response.getCode() == 200 && Boolean.TRUE.equals(response.getData());
            log.debug("Destino {} existe: {}", destinationId, exists);
            return exists;
        } catch (WebClientResponseException e) {
            log.error("Error HTTP al validar destino {}: {} - {}", destinationId, e.getStatusCode(), e.getResponseBodyAsString());
            return false;
        } catch (Exception e) {
            log.error("Error inesperado al validar destino {}: {}", destinationId, e.getMessage(), e);
            return false;
        }
    }

    // ==================== MÉTODOS CRUD ====================

    public ApiResponse<ReviewResponseDTO> createReview(String token, ReviewRequestDTO dto) {
        // 1. Validar token
        ApiResponse<UserDTO> authResponse = validateToken(token);
        if (authResponse.getCode() != 200 || authResponse.getData() == null) {
            log.warn("Token inválido o expirado al crear reseña");
            return new ApiResponse<>(401, "Token inválido o expirado", null);
        }
        UUID userId = authResponse.getData().getId();
        log.debug("Token válido. Usuario ID: {}", userId);

        // 2. Validar destino
        if (!destinationExists(dto.getDestinationId(), token)) {
            log.warn("Destino no existe: {}", dto.getDestinationId());
            return new ApiResponse<>(400, "El destino especificado no existe", null);
        }

        // 3. Crear entidad
        Review review = Review.builder()
                .destinationId(dto.getDestinationId())
                .userId(userId)
                .rating(dto.getRating())
                .comment(dto.getComment())
                .createdAt(LocalDateTime.now())
                .build();

        Review saved = reviewRepository.save(review);
        log.info("Reseña creada con ID: {}", saved.getId());

        return new ApiResponse<>(200, "Reseña creada exitosamente", mapToResponseDTO(saved));
    }

    public ApiResponse<ReviewResponseDTO> getReviewById(Long id) {
        return reviewRepository.findById(id)
                .map(review -> {
                    log.debug("Reseña encontrada ID: {}", id);
                    return new ApiResponse<>(200, "Reseña encontrada", mapToResponseDTO(review));
                })
                .orElseGet(() -> {
                    log.warn("Reseña no encontrada ID: {}", id);
                    return new ApiResponse<>(404, "Reseña no encontrada", null);
                });
    }

    public ApiResponse<List<ReviewResponseDTO>> getReviewsByDestination(UUID destinationId) {
        List<ReviewResponseDTO> list = reviewRepository.findByDestinationId(destinationId)
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
        log.debug("Se encontraron {} reseñas para el destino {}", list.size(), destinationId);
        return new ApiResponse<>(200, "Listado de reseñas para el destino", list);
    }

    public ApiResponse<ReviewResponseDTO> updateReview(String token, Long id, ReviewRequestDTO dto) {
        // 1. Validar token
        ApiResponse<UserDTO> authResponse = validateToken(token);
        if (authResponse.getCode() != 200 || authResponse.getData() == null) {
            log.warn("Token inválido al actualizar reseña ID: {}", id);
            return new ApiResponse<>(401, "Token inválido", null);
        }
        UUID userId = authResponse.getData().getId();

        // 2. Buscar reseña
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) {
            log.warn("Reseña no encontrada para actualizar ID: {}", id);
            return new ApiResponse<>(404, "Reseña no encontrada", null);
        }

        // 3. Verificar autoría
        if (!review.getUserId().equals(userId)) {
            log.warn("Usuario {} no autorizado para modificar reseña de usuario {}", userId, review.getUserId());
            return new ApiResponse<>(403, "No tienes permiso para modificar esta reseña", null);
        }

        // 4. Validar nuevo destino si cambió
        if (!review.getDestinationId().equals(dto.getDestinationId())) {
            if (!destinationExists(dto.getDestinationId(), token)) {
                log.warn("Nuevo destino no existe: {}", dto.getDestinationId());
                return new ApiResponse<>(400, "El destino especificado no existe", null);
            }
            review.setDestinationId(dto.getDestinationId());
        }

        // 5. Actualizar campos
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());

        Review updated = reviewRepository.save(review);
        log.info("Reseña actualizada ID: {}", updated.getId());

        return new ApiResponse<>(200, "Reseña actualizada", mapToResponseDTO(updated));
    }

    public ApiResponse<Void> deleteReview(String token, Long id) {
        // 1. Validar token
        ApiResponse<UserDTO> authResponse = validateToken(token);
        if (authResponse.getCode() != 200 || authResponse.getData() == null) {
            log.warn("Token inválido al eliminar reseña ID: {}", id);
            return new ApiResponse<>(401, "Token inválido", null);
        }
        UUID userId = authResponse.getData().getId();

        // 2. Buscar reseña
        Review review = reviewRepository.findById(id).orElse(null);
        if (review == null) {
            log.warn("Reseña no encontrada para eliminar ID: {}", id);
            return new ApiResponse<>(404, "Reseña no encontrada", null);
        }

        // 3. Verificar autoría
        if (!review.getUserId().equals(userId)) {
            log.warn("Usuario {} no autorizado para eliminar reseña de usuario {}", userId, review.getUserId());
            return new ApiResponse<>(403, "No tienes permiso para eliminar esta reseña", null);
        }

        // 4. Eliminar
        reviewRepository.deleteById(id);
        log.info("Reseña eliminada ID: {}", id);

        return new ApiResponse<>(200, "Reseña eliminada", null);
    }

    // ==================== MÉTODO AUXILIAR DE MAPEO ====================

    private ReviewResponseDTO mapToResponseDTO(Review review) {
        return ReviewResponseDTO.builder()
                .id(review.getId())
                .destinationId(review.getDestinationId())
                .userId(review.getUserId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .build();
    }
}