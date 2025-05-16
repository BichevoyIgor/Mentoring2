package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.bichevoy.entity.Role;
import ru.bichevoy.entity.User;
import ru.bichevoy.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class CustomUserDetailsServiceTest {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    private final String userName = "RandomUserName";

    @Test
    void loadUserByUsernameThrowException() {
        assertThrows(UsernameNotFoundException.class, () -> customUserDetailsService.loadUserByUsername(userName));
    }

    @Test
    @Transactional
    void loadUserByUsername() {
        User wishUser = new User("USERNAME" + LocalDateTime.now(),
                "EMAIL" + LocalDateTime.now(),
                passwordEncoder.encode("PASSWORD"),
                Role.ROLE_USER);

        User createdUser = userRepository.save(wishUser);

        assertEquals(createdUser.getName(), wishUser.getName());
        assertEquals(createdUser.getEmail(), wishUser.getEmail());
        assertEquals(createdUser.getName(), wishUser.getName());
        assertTrue(passwordEncoder.matches( "PASSWORD", createdUser.getPassword()));
    }
}