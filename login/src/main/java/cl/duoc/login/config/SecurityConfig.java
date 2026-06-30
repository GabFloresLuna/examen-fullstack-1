package cl.duoc.login.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactiva CSRF para facilitar pruebas con Postman y Swagger
            .csrf(csrf -> csrf.disable())
            // Configura autorización
            .authorizeHttpRequests(auth -> auth
                // Swagger y OpenAPI
                .requestMatchers(
                    "/v3/api-docs",
                    "/v3/api-docs/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/swagger-ui/index.html",
                    "/webjars/**"
                ).permitAll()
                // Actuator
                .requestMatchers("/actuator/**").permitAll()
                // Todas las demás rutas
                .anyRequest().permitAll()
            )
            // Desactiva login por formulario y HTTP Basic
            .formLogin(form -> form.disable())
            .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
