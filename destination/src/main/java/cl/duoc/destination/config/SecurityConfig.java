package cl.duoc.destination.config;

import cl.duoc.destination.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (Swagger y auth)
                .requestMatchers(
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/webjars/**",
                    "/api/v1/auth/**"   // login/register públicos
                ).permitAll()
                // Todo lo demás requiere token válido
                .anyRequest().authenticated()
            )
            // Filtro de validación JWT antes del filtro de autenticación por usuario/contraseña
            .addFilterBefore(new TokenValidationFilter(authService),
                             UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
