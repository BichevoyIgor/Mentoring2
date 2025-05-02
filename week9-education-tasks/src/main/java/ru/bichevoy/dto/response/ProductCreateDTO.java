package ru.bichevoy.dto.response;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProductCreateDTO(
        Long id,
        @NotNull String name,
        @Positive Double price,
        @NotNull String categoryName,
        @Positive Long categoryId
) {
}

