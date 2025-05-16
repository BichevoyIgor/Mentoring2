package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bichevoy.dto.user.UserRegistrationDto;
import ru.bichevoy.entity.Role;
import ru.bichevoy.entity.User;
import ru.bichevoy.exception.UserNameExistsException;
import ru.bichevoy.repository.UserRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User registerUserFromUserRegistrationDto(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsUserByName(userRegistrationDto.getName())) {
            throw new UserNameExistsException("Пользователь с таким именем уже есть в системе");
        }
        return userRepository.save(new User(userRegistrationDto.getName(),
                userRegistrationDto.getEmail(),
                passwordEncoder.encode(userRegistrationDto.getPassword()),
                Role.ROLE_USER));
    }

    public Optional<User> findUserByUserName(String username) {
        return userRepository.findUserByName(username);
    }
}
