package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.bichevoy.dto.user.UserRegistrationDto;
import ru.bichevoy.entity.Role;
import ru.bichevoy.entity.User;
import ru.bichevoy.exception.UserNameExistsException;
import ru.bichevoy.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class UserServiceTest {

    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final String USERNAME = "UserName";
    private final String EMAIL = "email@mail.ru";
    private final String PASSWORD = "password";

    @BeforeEach
    void init() {
        userRepository.save(new User(USERNAME,
                EMAIL,
                passwordEncoder.encode(PASSWORD),
                Role.ROLE_USER));
    }

    @Test
    void registerUserFromUserRegistrationDtoAndFindUserByUserName() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
        userRegistrationDto.setName(USERNAME);
        userRegistrationDto.setEmail(EMAIL);
        userRegistrationDto.setPassword(PASSWORD);

        assertThrows(UserNameExistsException.class, () -> userService.registerUserFromUserRegistrationDto(userRegistrationDto));

        userRegistrationDto.setName(USERNAME + "1");
        User creatdUser = userService.registerUserFromUserRegistrationDto(userRegistrationDto);
        User foundedUser = userService.findUserByUserName(creatdUser.getName()).get();

        assertEquals(creatdUser.getId(), foundedUser.getId());
        assertEquals(userRegistrationDto.getName(), foundedUser.getName());
        assertEquals(userRegistrationDto.getEmail(), foundedUser.getEmail());
        assertTrue(passwordEncoder.matches(userRegistrationDto.getPassword(), foundedUser.getPassword()));
    }

    @Test
    void findUserByUserName() {
        Optional<User> foundedUser = userRepository.findUserByName(USERNAME);
        assertTrue(foundedUser.isPresent());
        assertEquals(USERNAME, foundedUser.get().getName());
        assertEquals(EMAIL, foundedUser.get().getEmail());
    }
}