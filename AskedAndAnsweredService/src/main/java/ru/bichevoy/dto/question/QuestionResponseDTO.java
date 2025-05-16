package ru.bichevoy.dto.question;

import java.time.LocalDate;
import java.util.List;

public record QuestionResponseDTO(Long id,
                                  String header,
                                  String description,
                                  LocalDate createDate,
                                  String author,
                                  List<String> tags) {
}
