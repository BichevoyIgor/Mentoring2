package ru.bichevoy.dto.answer;

import org.springframework.stereotype.Component;
import ru.bichevoy.entity.Answer;

@Component
public class AnswerDTOMapper {

    public AnswerResponseDTO answerToDTO(Answer answer) {
        return new AnswerResponseDTO(
                answer.getId(),
                answer.getContent(),
                answer.getUser().getName(),
                answer.getDate()
        );
    }
}
