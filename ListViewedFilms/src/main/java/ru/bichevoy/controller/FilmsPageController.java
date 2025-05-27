package ru.bichevoy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bichevoy.dto.FilmDTORequest;
import ru.bichevoy.dto.FilmDTOResponse;
import ru.bichevoy.dto.GenreDTOResponses;
import ru.bichevoy.service.FilmService;

@Controller
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmsPageController {

    private final FilmService filmService;

    @GetMapping
    public String getFilms(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String genre,
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        Page<FilmDTOResponse> foundedFilms;
        if (genre == null || genre.isEmpty()) {
            foundedFilms = filmService.getFilmDTOList(page, size, currentUserName);
        } else {
            foundedFilms = filmService.getFilmDTOList(page, size, currentUserName, genre);
        }
        long countFoundedFilms = foundedFilms.getTotalElements();
        int totalPages = (int) Math.ceil(countFoundedFilms / (double) size);

        model.addAttribute("myCollection", "myCollection");
        model.addAttribute("currentUsername", authentication.getName());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("films", foundedFilms);
        model.addAttribute("genres",
                foundedFilms.get()
                        .flatMap(g -> g.genres().stream().map(GenreDTOResponses::title))
                        .toList());
        return "my_films_list";
    }

    @GetMapping("/add-film-form")
    public String getAddFilmForm() {
        return "film_form";
    }

    @PostMapping
    public String addFilmFromForm(FilmDTORequest filmDTORequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        filmService.saveFilmFromForm(filmDTORequest, authentication.getName());
        return "redirect:/films";
    }

    @PostMapping("/delete/{id}")
    public String deleteFilm(@PathVariable long id) {
        filmService.deleteFilmById(id);
        return "redirect:/films";
    }

    @GetMapping("/edit/{id}")
    public String getEditForm(@PathVariable long id, Model model) {
        model.addAttribute("film", filmService.getFilmDTOResponseById(id));
        return "film_form";
    }
}

