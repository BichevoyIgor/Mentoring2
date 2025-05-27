package ru.bichevoy.security.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bichevoy.dto.user.UserRegistrationDto;
import ru.bichevoy.service.UserService;

@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {

    private final UserService userService;

    @GetMapping
    public String showRegisterForm(Model model) {
        model.addAttribute("newUser", new UserRegistrationDto());
        return "register";
    }

    @PostMapping
    public String registerUser(@Valid UserRegistrationDto userRegistrationDto) {
        userService.registerUserFromUserRegistrationDto(userRegistrationDto);
        return "redirect:/login";
    }
}
