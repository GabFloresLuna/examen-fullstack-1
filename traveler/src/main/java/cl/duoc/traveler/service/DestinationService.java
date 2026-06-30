package cl.duoc.traveler.service;

import cl.duoc.traveler.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DestinationService {

    private final WebClient.Builder webClientBuilder;

    public ApiResponse<Boolean> validateDestination(UUID destinationId, String token) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("destination") // nombre lógico en Eureka
                        .path("/api/v1/destination/destinations/exists")
                        .queryParam("id", destinationId)
                        .build())
                    .header("Authorization", "Bearer " + token) // 🔑 pasar el token
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<Boolean>>() {})
                    .block();
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error al validar destino: " + e.getMessage(), null);
        }
    }
}
