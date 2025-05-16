package ru.bichevoy.dto.user;

import org.springframework.stereotype.Component;
import ru.bichevoy.entity.User;

@Component
public class UserRequestDTOMapper {

    public UserRequestDTO getUserRequestDTO(User user) {
        return new UserRequestDTO(user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole());
    }
}
