package ru.bichevoy.dto.user;

import ru.bichevoy.entity.User;

public record UserResponseDTO(Long id, String name) {
    public UserResponseDTO(User user) {
        this(user.getId(), user.getName());
    }
}
