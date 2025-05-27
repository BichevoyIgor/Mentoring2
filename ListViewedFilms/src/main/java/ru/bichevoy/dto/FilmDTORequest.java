package ru.bichevoy.dto;

public record FilmDTORequest(
        String id,
        String poster,
        String title,
        String year,
        String description,
        double rating,
        boolean share,
        String actors,
        String genres
) {
}
