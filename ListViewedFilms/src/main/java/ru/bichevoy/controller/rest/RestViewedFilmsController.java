package ru.bichevoy.controller.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.bichevoy.dto.ActorDTOResponse;
import ru.bichevoy.dto.FilmDTOResponse;
import ru.bichevoy.dto.GenreDTOResponses;
import ru.bichevoy.exception.PageSizeException;
import ru.bichevoy.service.ActorService;
import ru.bichevoy.service.FilmService;
import ru.bichevoy.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class RestViewedFilmsController {

    private final ActorService actorService;
    private final FilmService filmService;
    private final GenreService genreService;
    private final int MAX_PAGE_SIZE;

    public RestViewedFilmsController(GenreService genreService,
                                     ActorService actorService,
                                     @Value("${MAX_PAGE_SIZE_REST_RESPONSE}") int maxPageSize,
                                     FilmService filmService) {
        this.genreService = genreService;
        this.actorService = actorService;
        this.MAX_PAGE_SIZE = maxPageSize;
        this.filmService = filmService;
    }

    @GetMapping("/genres")
    public List<GenreDTOResponses> getGenres() {
        return genreService.getAllGenres().stream()
                .map(GenreDTOResponses::new)
                .toList();
    }

    @GetMapping("/actors")
    public Page<ActorDTOResponse> getActors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        checkSize(size);
        return actorService.findAllActorsDTOResponse(page, size);
    }

    @GetMapping("/films")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public Page<FilmDTOResponse> getFilmsByUserName(
            @RequestParam String userName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        checkSize(size);
        return filmService.getFilmDTOList(page, size, userName);
    }

    private void checkSize(int size) {
        if (size > MAX_PAGE_SIZE) {
            throw new PageSizeException(String.format("max page size = %d, current request size is %d",
                    MAX_PAGE_SIZE,
                    size));
        }
    }
}
