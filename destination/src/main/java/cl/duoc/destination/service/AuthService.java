package cl.duoc.destination.service;

import cl.duoc.destination.dto.ApiResponse;
import cl.duoc.destination.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final WebClient.Builder webClientBuilder;

    /**
     * Valida el token JWT contra el login-service registrado en Eureka.
     * @param token JWT recibido en el header Authorization
     * @return ApiResponse con el resultado de la validación y datos del usuario
     */
    public ApiResponse<UserDTO> validateToken(String token) {
        try {
            return webClientBuilder.build()
                    .get()
                    .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("login") // nombre lógico del login-service en Eureka
                        .path("/api/v1/users/validate")
                        .queryParam("token", token)
                        .build())
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<UserDTO>>() {})
                    .block();
        } catch (Exception e) {
            return new ApiResponse<>(500, "Error al validar token: " + e.getMessage(), null);
        }
    }
}
