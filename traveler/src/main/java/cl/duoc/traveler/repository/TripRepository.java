package cl.duoc.traveler.repository;

import cl.duoc.traveler.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TripRepository extends JpaRepository<Trip, UUID> {

    // Buscar viajes por usuario
    List<Trip> findByUserId(UUID userId);

    // Buscar viajes por destino
    List<Trip> findByDestinationId(UUID destinationId);

    // Buscar viajes por tipo (Business / Leisure)
    List<Trip> findByTravelType(String travelType);
}
