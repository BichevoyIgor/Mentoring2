package ru.bichevoy.dto.omb;

import java.io.Serializable;

public record OmbFilmShortDTO(
        String Title,
        String Year,
        String imdbID,
        String Type,
        String Poster
) implements Serializable {
}
