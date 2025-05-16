package ru.bichevoy.dto.question;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.Tag;

@Component
@RequiredArgsConstructor
public class QuestionDTOMapper {

    public QuestionResponseDTO getQuestionRequestDTO(Question question) {
        return new QuestionResponseDTO(
                question.getId(),
                question.getHeader(),
                question.getDescription(),
                question.getCreateDate(),
                question.getAuthor().getName(),
                question.getTags().stream()
                        .map(Tag::getName)
                        .toList()
        );
    }
}
