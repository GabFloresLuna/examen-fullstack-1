package cl.duoc.login.service;

import cl.duoc.login.dto.UserDTO;
import cl.duoc.login.model.User;
import cl.duoc.login.repository.UserRepository;
import cl.duoc.login.security.JwtUtil;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor // Lombok genera constructor con los campos final
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JwtUtil jwtUtil;

    // Registrar usuario con contraseña encriptada
    public User registerUser(String username, String rawPassword) {
        // Verificar si el usuario ya existe
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El usuario '" + username + "' ya existe");
        }

        String encodedPassword = passwordEncoder.encode(rawPassword);

        // El id se genera automáticamente en @PrePersist, no lo seteamos aquí
        User user = new User();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    // Validar login y generar token
    public String login(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(rawPassword, userOpt.get().getPassword())) {
            return jwtUtil.generateToken(username);
        }
        return null;
    }

    // Validar token JWT
    public UserDTO validateAndGetUser(String token) {
        if (jwtUtil.validateToken(token)) {
            String username = jwtUtil.extractUsername(token);
            return userRepository.findByUsername(username)
                    .map(user -> new UserDTO(user.getId(), user.getUsername()))
                    .orElse(null);
        }
        return null;
    }


    // Listar todos los usuarios
    public List<UserDTO> getAllUsersDTO() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getUsername()))
                .toList();
    }
}
