package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bichevoy.dto.FilmDTORequest;
import ru.bichevoy.dto.FilmDTOResponse;
import ru.bichevoy.dto.omb.OmbFilmFullDTO;
import ru.bichevoy.entity.Actor;
import ru.bichevoy.entity.Film;
import ru.bichevoy.entity.Genre;
import ru.bichevoy.exception.NotFoundException;
import ru.bichevoy.repository.FilmRepository;
import ru.bichevoy.service.omb.OmbService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {

    private final ActorService actorService;
    private final FilmRepository filmRepository;
    private final GenreService genreService;
    private final OmbService ombService;
    private final UserService userService;

    @Cacheable(value = "getFilmDTOList",
            key = "#page + '_size=' + #size + '_user=' + #userName")
    public Page<FilmDTOResponse> getFilmDTOList(int page, int size, String userName) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Film> all = filmRepository.findAllByUserName(pageable, userName);
        return all.map(FilmDTOResponse::new);
    }

    @Transactional
    @CacheEvict(value = "getFilmDTOList", allEntries = true)
    public void saveFilmFromAPI(String filmId, String userName) {
        OmbFilmFullDTO foundedFilm = ombService.findFilmByImdbID(filmId)
                .orElseThrow(() -> new NotFoundException("id фильма не верный"));
        Film film = new Film(foundedFilm.Poster(),
                foundedFilm.Title(),
                foundedFilm.Year(),
                foundedFilm.Plot(),
                Double.parseDouble(foundedFilm.imdbRating()),
                getActors(foundedFilm.Actors()),
                getGenres(foundedFilm.Genre()),
                userService.findUserByUserName(userName)
                        .orElseThrow(() -> new NotFoundException("id пользователя не найдено")));
        filmRepository.save(film);
    }

    @Transactional
    @CacheEvict(value = "getFilmDTOList", allEntries = true)
    public void saveFilmFromForm(FilmDTORequest filmDTORequest, String userName) {
        Film film;
        if (filmDTORequest.id().isEmpty()) {
            film = new Film();
        } else {
            film = filmRepository.findById(Long.parseLong(filmDTORequest.id()))
                    .orElseThrow(() -> new NotFoundException("id фильма не верный"));
        }
        film.setPoster(filmDTORequest.poster());
        film.setTitle(filmDTORequest.title().trim());
        film.setYear(filmDTORequest.year().trim());
        film.setDescription(filmDTORequest.description().trim());
        film.setRating(filmDTORequest.rating());
        film.setShare(filmDTORequest.share());
        film.getActors().clear();
        film.getActors().addAll(getActors(filmDTORequest.actors()));
        film.getGenres().clear();
        film.getGenres().addAll(getGenres(filmDTORequest.genres()));
        film.setUser(userService.findUserByUserName(userName)
                .orElseThrow(() -> new NotFoundException("id user не найден")));
        filmRepository.save(film);
    }

    private Set<Actor> getActors(String actors) {
        Set<Actor> actorsList = new HashSet<>();
        for (String actorName : actors.split(", ")) {
            Optional<Actor> actorByName = actorService.findActorByName(actorName.trim());
            if (actorByName.isPresent()) {
                actorsList.add(actorByName.get());
            } else {
                actorsList.add(actorService.saveActor(new Actor(actorName.trim())));
            }
        }
        return actorsList;
    }

    private Set<Genre> getGenres(String genres) {
        Set<Genre> genreList = new HashSet<>();
        for (String genre : genres.split(", ")) {
            Optional<Genre> genreByTitle = genreService.findGenreByTitle(genre.trim());
            if (genreByTitle.isPresent()) {
                genreList.add(genreByTitle.get());
            } else {
                genreList.add(genreService.saveGenre(new Genre(genre.trim())));
            }
        }
        return genreList;
    }

    @CacheEvict(value = "getFilmDTOList", allEntries = true)
    public void deleteFilmById(long id) {
        filmRepository.deleteById(id);
    }

    public FilmDTOResponse getFilmDTOResponseById(long id) {
        Film foundedFilm = filmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id фильма не верный"));
        return new FilmDTOResponse(foundedFilm);
    }

    public Page<FilmDTOResponse> getFilmDTOList(int page, int size, String userName, String genre) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Film> all = filmRepository.findAllByUserNameAndGenre(pageable, userName, genre);
        return all.map(FilmDTOResponse::new);
    }

    public Page<FilmDTOResponse> getSharedFilmDTOList(int page, int size, String userName) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Film> all = filmRepository.findAllSharedByUserName(pageable, userName);
        return all.map(FilmDTOResponse::new);
    }
}
