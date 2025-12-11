package main.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    Optional<Object> findByUsername(@NotBlank @Size(min = 3, max = 18) String username);
}
