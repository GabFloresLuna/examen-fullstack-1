package cl.duoc.traveler.repository;

import cl.duoc.traveler.model.TripDetails;
import cl.duoc.traveler.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TripDetailsRepository extends JpaRepository<TripDetails, UUID> {

    // Buscar detalles por viaje
    List<TripDetails> findByTrip(Trip trip);

    // Buscar detalles por clave (ej: "Transport", "Budget")
    List<TripDetails> findByDetailKey(String detailKey);
}
