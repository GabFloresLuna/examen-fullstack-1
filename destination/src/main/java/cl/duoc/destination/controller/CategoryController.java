package cl.duoc.destination.controller;

import cl.duoc.destination.dto.*;
import cl.duoc.destination.service.CategoryService;
import cl.duoc.destination.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Categories Controller", description = "Endpoints para gestión de categorías.")
@RestController
@RequestMapping("/api/v1/destination/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final AuthService authService;

    public CategoryController(CategoryService categoryService, AuthService authService) {
        this.categoryService = categoryService;
        this.authService = authService;
    }

    @PostMapping
    @Operation(summary = "Crear categoría", description = "Permite registrar una nueva categoría en el sistema.")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> createCategory(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody CategoryRequestDTO request) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        CategoryResponseDTO response = categoryService.createCategory(request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Categoría creada correctamente", response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener categoría por ID", description = "Permite consultar una categoría específica mediante su identificador único.")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> getCategoryById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        CategoryResponseDTO response = categoryService.getCategoryById(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Categoría encontrada", response));
    }

    @GetMapping
    @Operation(summary = "Listar categorías", description = "Permite obtener un listado de todas las categorías registradas en el sistema.")
    public ResponseEntity<ApiResponse<List<CategoryResponseDTO>>> getAllCategories(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        List<CategoryResponseDTO> response = categoryService.getAllCategories();
        return ResponseEntity.ok(new ApiResponse<>(200, "Listado de categorías", response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar categoría", description = "Permite modificar los datos de una categoría existente.")
    public ResponseEntity<ApiResponse<CategoryResponseDTO>> updateCategory(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id,
            @Valid @RequestBody CategoryRequestDTO request) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        CategoryResponseDTO response = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(new ApiResponse<>(200, "Categoría actualizada correctamente", response));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar categoría", description = "Permite eliminar una categoría existente mediante su identificador único.")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable UUID id) {

        String token = authHeader.replace("Bearer ", "");
        ApiResponse<UserDTO> validationResponse = authService.validateToken(token);

        if (validationResponse == null || validationResponse.getCode() != 200 || validationResponse.getData() == null) {
            return ResponseEntity.status(401)
                    .body(new ApiResponse<>(401, "Token inválido", null));
        }

        categoryService.deleteCategory(id);
        return ResponseEntity.ok(new ApiResponse<>(200, "Categoría eliminada correctamente", null));
    }
}
