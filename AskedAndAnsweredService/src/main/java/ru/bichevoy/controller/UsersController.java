package ru.bichevoy.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.bichevoy.entity.User;
import ru.bichevoy.entity.VoteType;
import ru.bichevoy.exception.NotFoundException;
import ru.bichevoy.service.AnswerService;
import ru.bichevoy.service.QuestionService;
import ru.bichevoy.service.UserService;
import ru.bichevoy.service.VoteService;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;
    private final QuestionService questionService;
    private final AnswerService answerService;
    private final VoteService voteService;

    public UsersController(UserService userService, QuestionService questionService, AnswerService answerService, VoteService voteService) {
        this.userService = userService;
        this.questionService = questionService;
        this.answerService = answerService;
        this.voteService = voteService;
    }

    @GetMapping("/{userName}")
    public String getUserDetailByName(@PathVariable String userName,
                                      Model model) {
        User user = userService.findUserByUserName(userName)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        model.addAttribute("userName", user.getName());
        model.addAttribute("countQuestions", questionService.findCountQuestionsByUser(user));
        model.addAttribute("countAnswer", answerService.findCountAnswerByUser(user));
        model.addAttribute("countLike", voteService.findCountVoteByUser(user, VoteType.UP));
        model.addAttribute("countDislike", voteService.findCountVoteByUser(user, VoteType.DOWN));
        return "userDetail";
    }
}

