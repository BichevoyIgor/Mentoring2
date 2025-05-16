package ru.bichevoy.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bichevoy.dto.answer.AnswerRequestDTO;
import ru.bichevoy.dto.question.QuestionRegistrationDTO;
import ru.bichevoy.dto.question.QuestionResponseDTO;
import ru.bichevoy.dto.vote.VoteDTO;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.Tag;
import ru.bichevoy.entity.User;
import ru.bichevoy.exception.NotFoundException;
import ru.bichevoy.service.AnswerService;
import ru.bichevoy.service.QuestionService;
import ru.bichevoy.service.TagService;
import ru.bichevoy.service.UserService;
import ru.bichevoy.service.VoteService;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/questions")
@RequiredArgsConstructor
public class QuestionAnswerController {

    private final QuestionService questionService;
    private final AnswerService answerService;
    private final UserService userService;
    private final VoteService voteService;
    private final TagService tagService;

    /**
     * Метод возвращает представление со списком всех вопросов
     */
    @GetMapping
    public String showQuestions(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", authentication.getName());
        model.addAttribute("questions", questionService.findAllQuestionsResponseDTO());
        model.addAttribute("vote", new VoteDTO());
        return "questions";
    }

    @GetMapping("/tagged/{tag}")
    public String showQuestionsByTag(@PathVariable String tag, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", authentication.getName());
        model.addAttribute("questions", questionService.findAllQuestionsResponseDTOByTagName(tag));
        model.addAttribute("vote", new VoteDTO());
        return "questions";
    }

    /**
     * Метод возвращает представление по конкретному вопросу
     */
    @GetMapping("/{id}")
    public String showQuestion(@PathVariable(name = "id") Long idQuestion,
                               Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", authentication.getName());
        QuestionResponseDTO question = questionService.findQuestionDTOById(idQuestion)
                .orElseThrow(()-> new NotFoundException(String.format("id = %d, не найден", idQuestion)));
        model.addAttribute("question", question);
        model.addAttribute("answers", answerService.findAllAnswerDTOByQuestionId(idQuestion));
        model.addAttribute("questionVotesMap", voteService.findCountQuestionVotes(idQuestion));
        model.addAttribute("answersVotes", voteService.findAllAnswerVotesByQuestion(idQuestion));
        return "question-detail";
    }

    /**
     * Метод возвращает представление с формой для нового вопроса
     */
    @GetMapping("/ask-question")
    public String showAskQuestionForm(Model model) {
        model.addAttribute("question", new QuestionRegistrationDTO());
        return "ask-question";
    }

    /**
     * Метод сохраняет новый вопрос в БД и перенаправляет запрос на список вопросов
     */
    @PostMapping("/new-question")
    public String registerNewQuestion(@Valid QuestionRegistrationDTO questionDTO, String tag) {
        Optional<Question> savedQuestion = questionService.saveNewQuestionDTO(questionDTO);
        if (tag != null && savedQuestion.isPresent()) {
            Tag newTag = tagService.findTagByName(tag).orElse(new Tag());
            newTag.setName(tag);
            savedQuestion.get().addTag(newTag);
            tagService.saveTag(newTag);
        }
        return "redirect:/questions";
    }

    /**
     * Метод возвращает представление с формой редактирования вопроса
     */
    @GetMapping("/{id}/edit")
    public String showEditQuestionForm(@PathVariable Long id, Model model) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (questionService.isQuestionAuthor(id, userName)) {
            model.addAttribute("question", questionService.findQuestionDTOById(id).orElseThrow(() -> new NotFoundException("Вопрос не найден")));
            return "edit-question";
        }
        return "redirect:/questions/" + id;
    }

    /**
     * Метод сохраняет отредактированный вопрос в БД
     */
    @PostMapping("/{id}/edit")
    public String editQuestion(@Valid QuestionRegistrationDTO question) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (questionService.isQuestionAuthor(question.getId(), user)) {
            questionService.updateQuestionFromDTO(question);
        }
        return "redirect:/questions/" + question.getId();
    }

    /**
     * Метод добавляет ответ к вопросу
     */
    @PostMapping("/{id}/add-answer")
    public String addAnswer(@PathVariable Long id, String answer) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> userOptional = userService.findUserByUserName(user);
        Optional<Question> question = questionService.findQuestionById(id);
        if (question.isPresent() && userOptional.isPresent()) {
            answerService.saveAnswer(new Answer(answer, LocalDate.now(), userOptional.get(), question.get()));
        }
        return "redirect:/questions/" + id;
    }

    /**
     * Метод возвращает представление с формой для редактирования ответа
     */
    @GetMapping("/{id-question}/edit_answer/{id-answer}")
    public String getAnswerEditForm(@PathVariable(name = "id-question") Long questionId,
                                    @PathVariable(name = "id-answer") Long answerId,
                                    Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("answerId", answerId);
        model.addAttribute("currentUsername", authentication.getName());
        QuestionResponseDTO question = questionService.findQuestionDTOById(questionId)
                .orElseThrow();
        model.addAttribute("answers", answerService.findAllAnswerDTOByQuestionId(questionId));
        model.addAttribute("question", question);
        return "edit-answer";
    }

    /**
     * Метод сохраняет измененный ответ
     */
    @PostMapping("/{id-question}/edit_answer/{id-answer}")
    public String editAnswer(@PathVariable(name = "id-question") Long questionId,
                             @PathVariable(name = "id-answer") Long answerId,
                             AnswerRequestDTO answerRequestDTO) {
        answerService.editAnswerFromAnswerRequestDTO(answerRequestDTO);
        return "redirect:/questions/" + questionId;
    }

    /**
     * Метод сохраняет голос за вопрос или ответ
     */
    @PostMapping("/vote")
    public String addVote(VoteDTO voteDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        voteDTO.setUserName(authentication.getName());
        voteService.saveVote(voteDTO);
        return "redirect:/questions/" + voteDTO.getQuestionId();
    }

    @PostMapping("/find-question")
    public String findQuestionsByQuestion(String question, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("currentUsername", authentication.getName());
        model.addAttribute("questions", questionService.findAllQuestionsResponseDTOByQuestion(question));
        model.addAttribute("vote", new VoteDTO());
        return "questions";
    }
}
