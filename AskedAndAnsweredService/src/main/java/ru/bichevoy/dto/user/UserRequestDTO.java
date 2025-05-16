package ru.bichevoy.dto.user;

import ru.bichevoy.entity.Role;

public record UserRequestDTO(Long id, String name, String email, Role role) {
}
