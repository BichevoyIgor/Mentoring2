package ru.bichevoy.dto.omb;

import java.io.Serializable;
import java.util.List;

public record OmbFilmsDTO(
        List<OmbFilmShortDTO> Search,
        Integer totalResults,
        String Response
) implements Serializable {
}
