package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.entity.Genre;
import ru.bichevoy.repository.GenreRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GenreService {

    private final GenreRepository genreRepository;

    public Genre saveGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    public Optional<Genre> findGenreByTitle(String title) {
        return genreRepository.findByTitle(title);
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }
}
