package cl.duoc.destination.repository;

import cl.duoc.destination.model.Destination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DestinationRepository extends JpaRepository<Destination, UUID> {
    boolean existsByNameAndCountry(String name, String country);
}
