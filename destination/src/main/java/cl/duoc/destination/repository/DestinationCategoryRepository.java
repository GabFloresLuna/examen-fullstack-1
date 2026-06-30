package cl.duoc.destination.repository;

import cl.duoc.destination.model.DestinationCategory;
import cl.duoc.destination.model.Destination;
import cl.duoc.destination.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DestinationCategoryRepository extends JpaRepository<DestinationCategory, UUID> {
    List<DestinationCategory> findByDestination(Destination destination);
    List<DestinationCategory> findByCategory(Category category);
}
