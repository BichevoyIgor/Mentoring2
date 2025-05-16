package ru.bichevoy.dto.vote;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.User;
import ru.bichevoy.entity.Vote;
import ru.bichevoy.repository.AnswerRepository;
import ru.bichevoy.service.UserService;

@Component
@RequiredArgsConstructor
public class VoteDTOMapper {

    private final AnswerRepository answerRepository;
    private final UserService userService;

    public Vote VoteDTOMapToVote(VoteDTO voteDTO) {
        Answer answer = answerRepository.findAnswerById(voteDTO.getAnswerId()).orElseThrow();
        User user = userService.findUserByUserName(voteDTO.getUserName()).orElseThrow();
        return new Vote(voteDTO.getVoteType(), user, answer.getQuestion(), answer);
    }
}
