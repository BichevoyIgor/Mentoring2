package ru.bichevoy.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bichevoy.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsUserByName(@NotBlank @Size(min = 2, max = 50) String name);

    Optional<User> findUserByName(String username);
}
