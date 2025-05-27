package ru.bichevoy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.bichevoy.dto.omb.OmbFilmFullDTO;
import ru.bichevoy.dto.omb.OmbFilmsDTO;
import ru.bichevoy.service.FilmService;
import ru.bichevoy.service.omb.OmbService;

@Controller
@RequestMapping("/find-film-in-external-API")
@RequiredArgsConstructor
public class FindInOmbController {

    private final FilmService filmService;
    private final OmbService ombService;

    @GetMapping
    public String findFilmInExternal(
            @RequestParam(defaultValue = "") String searchTitle,
            @RequestParam(defaultValue = "1") int page,
            Model model
    ) {
        if (searchTitle.isEmpty()) {
            return "redirect:/films";
        }

        OmbFilmsDTO foundedFilms = ombService.findFilmsByTitle(searchTitle, page);
        int countFoundedFilms = foundedFilms.totalResults();
        int totalPages = (int) Math.ceil(countFoundedFilms / (double) 10);

        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("searchTitle", searchTitle);
        model.addAttribute("films", foundedFilms);
        return "omb_films";
    }

    @GetMapping("/{id}")
    public String findFilmByImdbID(@PathVariable String id, Model model) {
        OmbFilmFullDTO foundedFilm = ombService.findFilmByImdbID(id).orElseThrow(() -> new RuntimeException("id фильма не верный"));
        model.addAttribute("film", foundedFilm);
        return "omb_film_detail";
    }

    @GetMapping("/add-in-library/{id}")
    public String addFilmFromAPIInLibrary(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        filmService.saveFilmFromAPI(id, authentication.getName());
        System.out.println();
        return "redirect:/films";
    }
}
