package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import ru.bichevoy.dto.question.QuestionDTOMapper;
import ru.bichevoy.dto.question.QuestionResponseDTO;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.Role;
import ru.bichevoy.entity.Tag;
import ru.bichevoy.entity.User;
import ru.bichevoy.repository.QuestionRepository;
import ru.bichevoy.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional
class QuestionServiceTest {

    private final QuestionDTOMapper questionDTOMapper;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final TagService tagService;
    private final QuestionService questionService;
    private final PasswordEncoder passwordEncoder;
    private final String USERNAME = "UserName";
    private final String EMAIL = "email@mail.ru";
    private final String PASSWORD = "password";
    private Question question;


    @BeforeEach
    void init() {
        User user = userRepository.save(new User(USERNAME,
                EMAIL,
                passwordEncoder.encode(PASSWORD),
                Role.ROLE_USER));

        question = questionRepository.save(new Question("Header",
                "description",
                LocalDate.now(),
                user));

        questionRepository.save(new Question("Header2",
                "description2",
                LocalDate.now(),
                user));

        questionRepository.save(new Question("Header3",
                "description3",
                LocalDate.now(),
                user));


    }

    @Test
    void findAllQuestionsResponseDTO() {
        assertTrue(questionService.findAllQuestionsResponseDTO().size() >= 3);
    }

    @Test
    void findAllQuestionsResponseDTOByTagName() {
        Tag tag = new Tag(LocalDateTime.now().toString());
        assertTrue(questionService.findAllQuestionsResponseDTOByTagName(tag.getName()).isEmpty());
        tagService.saveTag(tag);
        question.addTag(tag);
        questionRepository.save(question);
        List<QuestionResponseDTO> allQuestionsResponseDTOByTagName = questionService.findAllQuestionsResponseDTOByTagName(tag.getName());

        assertFalse(allQuestionsResponseDTOByTagName.isEmpty());
        String firstTag = allQuestionsResponseDTOByTagName.stream()
                .flatMap(q -> q.tags().stream())
                .filter(t -> t.equals(tag.getName()))
                .findFirst()
                .get();
        assertEquals(tag.getName(), firstTag);
    }

    @Test
    void findQuestionById() {
        Optional<Question> foundedQuestion = questionService.findQuestionById(question.getId());
        assertTrue(foundedQuestion.isPresent());
        assertEquals(foundedQuestion.get().getHeader(), question.getHeader());
        assertEquals(foundedQuestion.get().getDescription(), question.getDescription());
    }

    @Test
    void findQuestionDTOById() {
        QuestionResponseDTO wishDTO = questionDTOMapper.getQuestionRequestDTO(question);
        Optional<QuestionResponseDTO> foundedDTO = questionService.findQuestionDTOById(question.getId());
        assertTrue(foundedDTO.isPresent());
        assertEquals(wishDTO.header(), foundedDTO.get().header());
        assertEquals(wishDTO.description(), foundedDTO.get().description());
        assertEquals(wishDTO.author(), foundedDTO.get().author());
        assertTrue(wishDTO.tags().containsAll(foundedDTO.get().tags()));
    }

    @Test
    void isQuestionAuthor() {
        assertTrue(questionService.isQuestionAuthor(question.getId(), USERNAME));
        assertFalse(questionService.isQuestionAuthor(question.getId(), USERNAME + 1));
    }
}