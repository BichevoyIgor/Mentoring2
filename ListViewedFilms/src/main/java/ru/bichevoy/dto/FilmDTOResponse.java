package ru.bichevoy.dto;


import ru.bichevoy.entity.Film;

import java.io.Serializable;
import java.util.List;

public record FilmDTOResponse(
        Long id,
        String poster,
        String title,
        String year,
        String description,
        double rating,
        boolean share,
        List<ActorDTOResponse> actors,
        List<GenreDTOResponses> genres
) implements Serializable {

    public FilmDTOResponse(Film film) {
        this(
                film.getId(),
                film.getPoster(),
                film.getTitle(),
                film.getYear(),
                film.getDescription(),
                film.getRating(),
                film.isShare(),
                film.getActors().stream()
                        .map(ActorDTOResponse::new)
                        .toList(),
                film.getGenres().stream()
                        .map(GenreDTOResponses::new)
                        .toList()
        );
    }
}
