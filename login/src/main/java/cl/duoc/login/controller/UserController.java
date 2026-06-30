package cl.duoc.login.controller;

import cl.duoc.login.dto.ApiResponse;
import cl.duoc.login.dto.UserCredentialsDTO;
import cl.duoc.login.dto.UserDTO;
import cl.duoc.login.model.User;
import cl.duoc.login.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag(name = "User Controller", description = "Endpoints para gestión de usuarios y autenticación.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario", description = "Permite registrar un nuevo usuario con nombre de usuario y contraseña.")
    public ResponseEntity<ApiResponse<UserDTO>> register(@Valid @RequestBody UserCredentialsDTO dto) {
        try {
            User newUser = userService.registerUser(dto.getUsername(), dto.getPassword());
            UserDTO userDTO = new UserDTO(newUser.getId(), newUser.getUsername());

            ApiResponse<UserDTO> response =
                    new ApiResponse<>(200, "Usuario registrado correctamente", userDTO);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error al registrar usuario: {}", e.getMessage());
            ApiResponse<UserDTO> response =
                    new ApiResponse<>(400, "Error al registrar usuario: " + e.getMessage(), null);
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario iniciar sesión con su nombre de usuario y contraseña, retornando un token de autenticación si las credenciales son válidas.")
    public ResponseEntity<ApiResponse<String>> login(@Valid @RequestBody UserCredentialsDTO dto) {
        String token = userService.login(dto.getUsername(), dto.getPassword());

        if (token != null) {
            ApiResponse<String> response =
                    new ApiResponse<>(200, "Login exitoso", token);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<String> response =
                    new ApiResponse<>(401, "Credenciales inválidas", null);
            return ResponseEntity.status(401).body(response);
        }
    }

    @GetMapping("/list")
    @Operation(summary = "Listar usuarios", description = "Permite obtener una lista de todos los usuarios registrados en el sistema.")
    public ResponseEntity<ApiResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsersDTO();
        ApiResponse<List<UserDTO>> response =
                new ApiResponse<>(200, "Listado de usuarios", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/validate")
    @Operation(summary = "Validar token", description = "Permite validar un token de autenticación y obtener los datos del usuario asociado.")
    public ResponseEntity<ApiResponse<UserDTO>> validateToken(@RequestParam String token) {
        UserDTO userDTO = userService.validateAndGetUser(token);

        if (userDTO != null) {
            ApiResponse<UserDTO> response =
                    new ApiResponse<>(200, "Token válido", userDTO);
            return ResponseEntity.ok(response);
        } else {
            ApiResponse<UserDTO> response =
                    new ApiResponse<>(401, "Token inválido", null);
            return ResponseEntity.status(401).body(response);
        }
    }

}
