package ru.bichevoy.dto;

import ru.bichevoy.entity.Genre;

import java.io.Serializable;

public record GenreDTOResponses(long id, String title) implements Serializable {

    public GenreDTOResponses(Genre genre) {
        this(genre.getId(), genre.getTitle());
    }
}
