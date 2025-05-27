package ru.bichevoy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bichevoy.dto.FilmDTOResponse;
import ru.bichevoy.entity.User;
import ru.bichevoy.exception.NotFoundException;
import ru.bichevoy.service.FilmService;
import ru.bichevoy.service.UserService;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;
    private final FilmService filmService;

    @GetMapping
    public String getProfilePage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", authentication.getName());
        model.addAttribute("friends", userService.findFriends(authentication.getName()));
        return "profile";
    }

    @GetMapping("/addFriend")
    public String addFriend(@RequestParam String friendName) {
        userService.addFriend(friendName);
        return "redirect:/profile";
    }

    @GetMapping("/{friendName}")
    public String getFriendProfile(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @PathVariable String friendName,
            Model model
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User currentUser = userService.findUserByUserName(auth.getName())
                .orElseThrow(() -> new NotFoundException("Логин не найден"));

        User friend = currentUser.getFriends().stream()
                .filter(f -> f.getName().equals(friendName))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Логин не найден"));

        if (!friend.getFriends().contains(currentUser)) {
            throw new NotFoundException("Вы не добавлены в друзья к " + friend.getName());
        }

        Page<FilmDTOResponse> foundedFilms = filmService.getSharedFilmDTOList(page, size, friendName);
        long countFoundedFilms = foundedFilms.getTotalElements();
        int totalPages = (int) Math.ceil(countFoundedFilms / (double) size);

        model.addAttribute("friendFilms", foundedFilms);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        return "profile";
    }
}
