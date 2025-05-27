package ru.bichevoy.dto.omb;

import java.io.Serializable;

public record OmbFilmFullDTO(
        String Title,
        String Year,
        String Released,
        String Genre,
        String Actors,
        String Plot,
        String Poster,
        String imdbRating,
        String imdbID
        ) implements Serializable {
}
