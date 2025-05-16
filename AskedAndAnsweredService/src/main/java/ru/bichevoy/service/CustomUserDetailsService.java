package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.bichevoy.entity.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUserName(username).orElseThrow(() ->
                new UsernameNotFoundException("Пользователь с таким именем не найден" + username));

        return new org.springframework.security.core.userdetails.User(user.getName(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority(user.getRole().toString())));
    }
}
