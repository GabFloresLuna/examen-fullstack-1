package cl.duoc.login.repository;

import cl.duoc.login.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Buscar usuario por username
    Optional<User> findByUsername(String username);
}
