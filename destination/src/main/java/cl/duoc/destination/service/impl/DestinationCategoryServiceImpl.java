package cl.duoc.destination.service.impl;

import cl.duoc.destination.dto.DestinationCategoryRequestDTO;
import cl.duoc.destination.dto.DestinationCategoryResponseDTO;
import cl.duoc.destination.model.Category;
import cl.duoc.destination.model.Destination;
import cl.duoc.destination.model.DestinationCategory;
import cl.duoc.destination.repository.CategoryRepository;
import cl.duoc.destination.repository.DestinationRepository;
import cl.duoc.destination.repository.DestinationCategoryRepository;
import cl.duoc.destination.service.DestinationCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DestinationCategoryServiceImpl implements DestinationCategoryService {

    private final DestinationCategoryRepository destinationCategoryRepository;
    private final DestinationRepository destinationRepository;
    private final CategoryRepository categoryRepository;

    public DestinationCategoryServiceImpl(DestinationCategoryRepository destinationCategoryRepository,
                                          DestinationRepository destinationRepository,
                                          CategoryRepository categoryRepository) {
        this.destinationCategoryRepository = destinationCategoryRepository;
        this.destinationRepository = destinationRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public DestinationCategoryResponseDTO createDestinationCategory(DestinationCategoryRequestDTO request) {
        Destination destination = destinationRepository.findById(request.getDestinationId())
                .orElseThrow(() -> new RuntimeException("Destino no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        DestinationCategory destinationCategory = DestinationCategory.builder()
                .destination(destination)
                .category(category)
                .build();

        DestinationCategory saved = destinationCategoryRepository.save(destinationCategory);
        return toResponse(saved);
    }

    @Override
    public List<DestinationCategoryResponseDTO> getAllDestinationCategories() {
        return destinationCategoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDestinationCategory(UUID id) {
        if (!destinationCategoryRepository.existsById(id)) {
            throw new RuntimeException("Relación destino-categoría no encontrada");
        }
        destinationCategoryRepository.deleteById(id);
    }

    private DestinationCategoryResponseDTO toResponse(DestinationCategory destinationCategory) {
        return DestinationCategoryResponseDTO.builder()
                .id(destinationCategory.getId())
                .destinationId(destinationCategory.getDestination().getId())
                .categoryId(destinationCategory.getCategory().getId())
                .build();
    }
}
