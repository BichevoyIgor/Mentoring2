package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.bichevoy.dto.user.UserRegistrationDto;
import ru.bichevoy.dto.user.UserResponseDTO;
import ru.bichevoy.entity.Role;
import ru.bichevoy.entity.User;
import ru.bichevoy.exception.NotFoundException;
import ru.bichevoy.exception.UserNameExistsException;
import ru.bichevoy.repository.UserRepository;

import java.util.List;
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

    public List<UserResponseDTO> findFriends(String userName) {
        User foundedUser = userRepository.findUserByName(userName)
                .orElseThrow(() -> new UserNameExistsException("User not found"));
        return foundedUser.getFriends().stream()
                .map(UserResponseDTO::new).toList();
    }

    public void addFriend(String friendName) {
        Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
        User friend = userRepository.findUserByName(friendName)
                .orElseThrow(() -> new NotFoundException("Логин друга не найден"));
        User user = userRepository.findUserByName(currentUser.getName())
                .orElseThrow(() -> new NotFoundException("Логин не найден"));
        user.getFriends().add(friend);
        userRepository.save(user);
    }
}
