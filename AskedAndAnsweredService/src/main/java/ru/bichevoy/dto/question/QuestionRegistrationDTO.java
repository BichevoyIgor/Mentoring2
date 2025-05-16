package ru.bichevoy.dto.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QuestionRegistrationDTO {
    private Long id;
    @NotBlank
    private String header;
    @NotBlank
    private String description;
    private List<String> tags;
}
