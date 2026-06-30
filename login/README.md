# Checklist – Integración de JWT en Login-Service

## Cambios realizados

- [x] **Agregar dependencias necesarias en `pom.xml`**
  - `jjwt` (Java JWT) para generación y validación de tokens.
  - `spring-boot-starter-security` para habilitar seguridad en el proyecto.

- [x] **Crear clase utilitaria `JwtUtil.java`**
  - Métodos para generar tokens (`generateToken`).
  - Métodos para validar tokens (`validateToken`).
  - Métodos para extraer claims (ej. `getUsernameFromToken`).

- [x] **Ajustar `UserService`**
  - En el método `login`, devolver un JWT en lugar de solo un mensaje de bienvenida.
  - Agregar método `validateToken(String token)` que use `JwtUtil`.

- [x] **Modificar `UserController`**
  - En `/login`, devolver un `ApiResponse<String>` con el token JWT en el campo `data`.
  - Crear endpoint `/validate` que reciba un token y devuelva `200 Token válido` o `401 Token inválido`.

- [x] **Crear clase `SecurityConfig.java`**
  - Configurar Spring Security para desactivar CSRF.
  - Definir rutas públicas (`/api/v1/users/login`, `/api/v1/users/register`, `/api/v1/users/validate`).
  - Proteger todas las demás rutas con autenticación.

- [x] **(Opcional) Implementar `JwtAuthenticationFilter`**
  - Extiende `OncePerRequestFilter`.
  - Intercepta cada request, extrae el token del header `Authorization`.
  - Valida el token con `JwtUtil`.
  - Si es válido, registra al usuario en el `SecurityContext`.

---

## Comandos y pruebas

1. **Generar token en login-service**
   ```bash
   POST http://localhost:9000/api/v1/users/login
   Body: { "username": "user", "password": "pass" }

## Ajustes Varios

1. **Pruaba de Commit**

