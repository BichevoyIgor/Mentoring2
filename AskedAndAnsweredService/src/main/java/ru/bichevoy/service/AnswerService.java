package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dto.answer.AnswerDTOMapper;
import ru.bichevoy.dto.answer.AnswerRequestDTO;
import ru.bichevoy.dto.answer.AnswerResponseDTO;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.User;
import ru.bichevoy.repository.AnswerRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final AnswerDTOMapper answerDTOMapper;

    public void saveAnswer(Answer answer) {
        answerRepository.save(answer);
    }

    /**
     * Метод возвращает список ответов(DTO) если они есть для указанного id вопроса
     */
    public List<AnswerResponseDTO> findAllAnswerDTOByQuestionId(Long id) {
        List<Answer> allAnswer = answerRepository.findAllByQuestionId(id);
        return allAnswer.stream()
                .map(answerDTOMapper::answerToDTO)
                .sorted(Comparator.comparingLong(AnswerResponseDTO::id))
                .toList();
    }

    /**
     * Метод изменения тела ответа по запросу с web
     */
    public void editAnswerFromAnswerRequestDTO(AnswerRequestDTO answerRequestDTO) {
        Optional<Answer> answerOpt = answerRepository.findAnswerById(answerRequestDTO.id());
        if (answerOpt.isPresent()) {
            Answer answer = answerOpt.get();
            answer.setContent(answerRequestDTO.content());
            answerRepository.save(answer);
        }
    }

    /**
     * Метод возвращает количество ответов пользователя
     */
    public Long findCountAnswerByUser(User user) {
        return answerRepository.countAnswerByUser(user);
    }
}
