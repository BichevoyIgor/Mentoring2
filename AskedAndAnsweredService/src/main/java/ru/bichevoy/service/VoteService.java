package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dto.vote.VoteAnswer;
import ru.bichevoy.dto.vote.VoteDTO;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.User;
import ru.bichevoy.entity.Vote;
import ru.bichevoy.entity.VoteType;
import ru.bichevoy.repository.AnswerRepository;
import ru.bichevoy.repository.VoteRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {
    private final VoteRepository voteRepository;
    private final UserService userService;
    private final AnswerRepository answerRepository;
    private final QuestionService questionService;

    /**
     * Метод сохранения голоса
     */
    public void saveVote(VoteDTO voteDTO) {
        if (voteDTO.getAnswerId() == null) {
            saveQuestionVote(voteDTO);
        } else {
            saveAnswerVote(voteDTO);
        }
    }

    /**
     * Метод сохранения голоса за вопрос
     */
    private void saveQuestionVote(VoteDTO voteDTO) {
        User user = userService.findUserByUserName(voteDTO.getUserName()).orElseThrow();
        Question question = questionService.findQuestionById(voteDTO.getQuestionId()).orElseThrow();
        Vote vote = voteRepository.findQuestionVote(user, question)
                .orElse(new Vote(voteDTO.getVoteType(), user, question, null));
        if (vote.getId() == null || !vote.getVoteType().equals(voteDTO.getVoteType())) {
            vote.setVoteType(voteDTO.getVoteType());
            voteRepository.save(vote);
        }
    }

    /**
     * Метод сохранения голоса ответ
     */
    private void saveAnswerVote(VoteDTO voteDTO) {
        User user = userService.findUserByUserName(voteDTO.getUserName()).orElseThrow();
        Answer answer = answerRepository.findAnswerById(voteDTO.getAnswerId()).orElseThrow();
        Vote vote = voteRepository.findAnswerVote(user, answer.getQuestion(), answer)
                .orElse(new Vote(voteDTO.getVoteType(), user, answer.getQuestion(), answer));
        if (vote.getId() == null || !vote.getVoteType().equals(voteDTO.getVoteType())) {
            vote.setVoteType(voteDTO.getVoteType());
            voteRepository.save(vote);
        }
    }

    /**
     * Метод возвращает количество голосов отданных за like и dislike
     */
    public Map<String, Integer> findCountQuestionVotes(Long idQuestion) {
        Map<String, Integer> mapVotes = voteRepository.findVotesForQuestion(idQuestion).stream()
                .collect(Collectors.toMap(
                        arr -> (String) arr[0],
                        arr -> ((Number) arr[1]).intValue()
                ));
        mapVotes.putIfAbsent(VoteType.UP.name(), 0);
        mapVotes.putIfAbsent(VoteType.DOWN.name(), 0);
        return mapVotes;
    }

    /**
     * Метод возвращает количество голосов отданных за like и dislike по каждому ответу
     */
    public Map<Long, VoteAnswer> findAllAnswerVotesByQuestion(Long idQuestion) {
        List<Object[]> answerVotesForQuestion = voteRepository.findAnswerVotesForQuestion(idQuestion);
        Map<Long, VoteAnswer> map = new HashMap<>();
        for (Object[] objects : answerVotesForQuestion) {
            Long answerId = (Long) objects[0];
            String voteType = (String) objects[1];
            Long count = (Long) objects[2];

            VoteAnswer orDefault = map.getOrDefault(answerId, new VoteAnswer());
            if (voteType.equals(VoteType.UP.name())) {
                orDefault.setCountUP(count);
            } else if (voteType.equals(VoteType.DOWN.name())) {
                orDefault.setCountDown(count);
            }
            map.put(answerId, orDefault);
        }
        return map;
    }

    /**
     * Метод возвращает количество голосов отданных за like и dislike
     */
    public Long findCountVoteByUser(User user, VoteType voteType) {
        return voteRepository.countVotesByUserAndVoteType(user, voteType);
    }
}
