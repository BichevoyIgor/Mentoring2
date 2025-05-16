package ru.bichevoy.controller.rest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.Role;
import ru.bichevoy.entity.User;
import ru.bichevoy.repository.QuestionRepository;
import ru.bichevoy.repository.UserRepository;
import ru.bichevoy.service.AnswerService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@WithMockUser(username = "user", authorities = "ROLE_ADMIN")
@Transactional
class ControllerTest {

    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MockMvc mockMvc;
    private final AnswerService answerService;

    private User user;
    private Question question;
    private Answer answer;

    @BeforeEach
    void init() {
        user = userRepository.save(new User("user",
                "EMAIL@mail.ru",
                passwordEncoder.encode("PASSWORD"),
                Role.ROLE_ADMIN));

        question = questionRepository.save(new Question("Header",
                "description",
                LocalDate.now(),
                user));
        answer = new Answer("answer", LocalDate.now(), user, question);
        answerService.saveAnswer(answer);
    }

    @Test
    void getAllQuestion() throws Exception {
        mockMvc.perform(get("/api/v1/questions"))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getQuestionById() throws Exception {
        mockMvc.perform(get(String.format("/api/v1/questions/%d", question.getId())))
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json(String.format("""
                                {
                                  "id": %d,
                                  "header": "Header",
                                  "description": "description",
                                  "createDate": %s,
                                  "author": %s,
                                  "tags": []
                                }
                                """, question.getId(), question.getCreateDate(), question.getAuthor().getName()))
                );
    }

    @Test
    void createNewQuestion() throws Exception {
        mockMvc.perform(post("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "header": "creating_header",
                                  "description": "creating_string",
                                  "createDate": "2025-05-16",
                                  "tags": [
                                  ]
                                }
                                """))
                .andExpectAll(
                        status().isCreated(),
                        content().json("""
                                {
                                  "header": "creating_header",
                                  "description": "creating_string",
                                  "createDate": "2025-05-16"
                                }
                                """),
                        jsonPath("$.id").exists()
                );
    }

    @Test
    void changeQuestion() throws Exception {
        mockMvc.perform(put("/api/v1/questions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                  "id": "%d",
                                  "header": "%s",
                                  "description": "string",
                                  "tags": []
                                }
                                """, question.getId(), question.getHeader())))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$.description").value("string")
                );
    }

    @Test
    void deleteQuestion() throws Exception {
        mockMvc.perform(delete(String.format("/api/v1/questions/%d", question.getId())))
                .andExpect(status().isOk());
    }
}