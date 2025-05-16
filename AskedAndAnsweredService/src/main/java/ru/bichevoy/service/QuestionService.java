package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.bichevoy.dto.question.QuestionDTOMapper;
import ru.bichevoy.dto.question.QuestionRegistrationDTO;
import ru.bichevoy.dto.question.QuestionResponseDTO;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.Tag;
import ru.bichevoy.entity.User;
import ru.bichevoy.exception.NotFoundException;
import ru.bichevoy.repository.QuestionRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionDTOMapper questionDTOMapper;
    private final UserService userService;
    private final TagService tagService;

    /**
     * Метод возвращает список DTO сущностей вопросов
     */
    public List<QuestionResponseDTO> findAllQuestionsResponseDTO() {
        return questionRepository.findAll().stream()
                .map(questionDTOMapper::getQuestionRequestDTO)
                .toList();
    }

    /**
     * Метод возвращает список DTO сущностей вопросов по имени тега
     */
    public List<QuestionResponseDTO> findAllQuestionsResponseDTOByTagName(String tagName) {
        return questionRepository.findAllByTagName(tagName).stream()
                .map(questionDTOMapper::getQuestionRequestDTO)
                .toList();
    }

    /**
     * Метод возвращает вопрос по его id
     */
    public Optional<Question> findQuestionById(Long id) {
        return questionRepository.findById(id);
    }

    /**
     * Метод возвращает DTO сущности вопроса
     */
    public Optional<QuestionResponseDTO> findQuestionDTOById(Long id) {
        Optional<Question> foundedQuestion = questionRepository.findById(id);
        return foundedQuestion.map(questionDTOMapper::getQuestionRequestDTO);
    }

    /**
     * Метод сохраняет вопрос из DTO сущности в БД
     */
    public Optional<Question> saveNewQuestionDTO(QuestionRegistrationDTO questionDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> author = userService.findUserByUserName(authentication.getName());

        Question question = new Question(questionDTO.getHeader(),
                questionDTO.getDescription(),
                LocalDate.now(),
                author.orElseThrow(() -> new NotFoundException("Пользователь не найден")));

        if (questionDTO.getTags() != null) {
            for (String tag : questionDTO.getTags()) {
                Optional<Tag> tagByName = tagService.findTagByName(tag);
                if (tagByName.isPresent()) {
                    question.addTag(tagByName.get());
                } else {
                    Tag createdTag = new Tag(tag);
                    tagService.saveTag(createdTag);
                    question.addTag(createdTag);
                }
            }
        }
        questionRepository.save(question);
        return Optional.of(question);
    }

    /**
     * Метод проверяет, является ли пользователь, автором вопроса
     */
    public boolean isQuestionAuthor(Long idQuestion, String authorName) {
        Question question = questionRepository.findById(idQuestion).orElseThrow(() -> new NotFoundException("Вопрос не найден"));
        return question.getAuthor().getName().equals(authorName);
    }

    /**
     * Метод сохраняет изменения из DTO сущности вопроса в БД
     */
    public Question updateQuestionFromDTO(QuestionRegistrationDTO questionDTO) {
        Question foundedQuestion = questionRepository.findById(questionDTO.getId())
                .orElseThrow(() -> new NotFoundException(String.format("Вопрос с id=%d не найден", questionDTO.getId())));
        foundedQuestion.setHeader(questionDTO.getHeader());
        foundedQuestion.setDescription(questionDTO.getDescription());
        if (!questionDTO.getTags().isEmpty()) {
            Set<Tag> tagSet = new HashSet<>();
            for (String tag : questionDTO.getTags()) {
                Tag foundedTag = tagService.findTagByName(tag).orElse(new Tag(tag));
                tagSet.add(foundedTag);
            }
            foundedQuestion.setTags(tagSet);
        }
        return questionRepository.save(foundedQuestion);
    }

    /**
     * Метод возвращает список DTO сущностей вопросов по ключевому слову
     */
    public List<QuestionResponseDTO> findAllQuestionsResponseDTOByQuestion(String question) {
        return questionRepository.findByHeaderContainingIgnoreCase(question).stream()
                .map(questionDTOMapper::getQuestionRequestDTO)
                .toList();
    }

    /**
     * Метод возвращает количество вопросов заданных пользователем
     */
    public Long findCountQuestionsByUser(User user) {
        return questionRepository.countQuestionByAuthor(user);
    }

    /**
     * Метод удаляет вопрос по его id
     */
    public void deleteQuestionById(Long id) {
        questionRepository.deleteById(id);
    }
}
