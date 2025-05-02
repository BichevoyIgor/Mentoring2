package ru.bichevoy.dto.response;

public record ProductResponseDTO(
        Long id,
        String name,
        Double price,
        String categoryName,
        Long categoryId
) {
}
