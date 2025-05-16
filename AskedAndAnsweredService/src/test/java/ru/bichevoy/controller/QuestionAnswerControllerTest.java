package ru.bichevoy.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.Role;
import ru.bichevoy.entity.User;
import ru.bichevoy.entity.VoteType;
import ru.bichevoy.repository.QuestionRepository;
import ru.bichevoy.repository.UserRepository;
import ru.bichevoy.service.AnswerService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@ActiveProfiles("test")
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
@RequiredArgsConstructor
@WithMockUser(username = "user", authorities = "USER")
@Transactional
class QuestionAnswerControllerTest {

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
        user = userRepository.save(new User("author",
                "EMAIL@mail.ru",
                passwordEncoder.encode("PASSWORD"),
                Role.ROLE_USER));

        question = questionRepository.save(new Question("Header",
                "description",
                LocalDate.now(),
                user));
        answer = new Answer("answer", LocalDate.now(), user, question);
        answerService.saveAnswer(answer);
    }

    @Test
    void showQuestions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/questions"))
                .andExpect(model().attribute("currentUsername", "user"))
                .andExpect(model().attributeExists("questions", "vote"))
                .andExpect(view().name("questions"));
    }

    @Test
    void showQuestionsByTag() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/questions/tagged/tag"))
                .andExpect(view().name("questions"));
    }

    @Test
    void showQuestion() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/questions/%d", question.getId())))
                .andExpect(view().name("question-detail"));
    }

    @Test
    void showAskQuestionForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/questions/ask-question"))
                .andExpect(view().name("ask-question"));
    }

    @Test
    void registerNewQuestion() throws Exception {
        userRepository.save(new User("user",
                "EMAIL@mail.ru",
                passwordEncoder.encode("PASSWORD"),
                Role.ROLE_USER));

        mockMvc.perform(post("/questions/new-question")
                        .with(csrf())
                        .param("header", "header")
                        .param("description", "description")
                        .param("tag", "tag"))
                .andExpect(redirectedUrl("/questions"));
    }

    @Test
    void showEditQuestionForm() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/questions/%d/edit", question.getId()))
                        .with(SecurityMockMvcRequestPostProcessors.user(user.getName())))
                .andExpect(view().name("edit-question"));
    }

    @Test
    @Transactional
    void showEditQuestionFormIfNotAuthor() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(String.format("/questions/%d/edit", question.getId()))
                        .with(SecurityMockMvcRequestPostProcessors.user("NoUser")))
                .andExpect(redirectedUrl("/questions/" + question.getId()));
    }

    @Test
    void editQuestion() throws Exception {
        mockMvc.perform(post(String.format("/questions/%d/edit", question.getId()))
                        .with(csrf())
                        .param("id", String.valueOf(question.getId()))
                        .param("header", "header")
                        .param("description", "description")
                        .param("tags", String.valueOf(List.of("tag"))))
                .andExpect(redirectedUrl("/questions/" + question.getId()));
    }

    @Test
    void addAnswer() throws Exception {
        mockMvc.perform(post(String.format("/questions/%d/add-answer", question.getId()))
                        .with(csrf())
                        .param("id", String.valueOf(question.getId()))
                        .param("answer", "answer"))
                .andExpect(redirectedUrl("/questions/" + question.getId()));
    }

    @Test
    void getAnswerEditForm() throws Exception {
        mockMvc.perform(get(String.format("/questions/%d/edit_answer/%d", question.getId(), answer.getId())))
                .andExpect(view().name("edit-answer"));
    }

    @Test
    void editAnswer() throws Exception {
        mockMvc.perform(post(String.format("/questions/%d/edit_answer/%d", question.getId(), answer.getId()))
                        .with(csrf())
                        .param("id", String.valueOf(answer.getId()))
                        .param("content", "newContent"))
                .andExpect(redirectedUrl("/questions/" + question.getId()));
    }

    @Test
    void addVoteQuestion() throws Exception {
        userRepository.save(new User("user",
                "EMAIL@mail.ru",
                passwordEncoder.encode("PASSWORD"),
                Role.ROLE_USER));

        mockMvc.perform(post("/questions/vote")
                        .with(csrf())
                        .param("voteType", VoteType.UP.name())
                        .param("questionId", String.valueOf(question.getId()))
                        .param("user", "user"))
                .andExpect(redirectedUrl("/questions/" + question.getId()));
    }

    @Test
    void addVoteAnswer() throws Exception {
        userRepository.save(new User("user",
                "EMAIL@mail.ru",
                passwordEncoder.encode("PASSWORD"),
                Role.ROLE_USER));

        mockMvc.perform(post("/questions/vote")
                        .with(csrf())
                        .param("voteType", VoteType.UP.name())
                        .param("questionId", String.valueOf(question.getId()))
                        .param("answerId", String.valueOf(answer.getId()))
                        .param("user", "user"))
                .andExpect(redirectedUrl("/questions/" + question.getId()));
    }

    @Test
    void findQuestionsByQuestion() throws Exception {
        mockMvc.perform(post("/questions/find-question")
                        .with(csrf())
                        .param("question", "Header"))
                .andExpect(view().name("questions"));
    }
}