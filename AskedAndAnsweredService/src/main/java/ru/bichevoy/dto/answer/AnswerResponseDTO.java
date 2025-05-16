package ru.bichevoy.dto.answer;

import java.time.LocalDate;

public record AnswerResponseDTO(
        Long id,
        String content,
        String userName,
        LocalDate date) {
}
