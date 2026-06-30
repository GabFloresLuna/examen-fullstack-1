package cl.duoc.destination.service;

import cl.duoc.destination.dto.CategoryRequestDTO;
import cl.duoc.destination.dto.CategoryResponseDTO;

import java.util.List;
import java.util.UUID;

public interface CategoryService {
    CategoryResponseDTO createCategory(CategoryRequestDTO request);
    CategoryResponseDTO getCategoryById(UUID id);
    List<CategoryResponseDTO> getAllCategories();
    CategoryResponseDTO updateCategory(UUID id, CategoryRequestDTO request);
    void deleteCategory(UUID id);
}
