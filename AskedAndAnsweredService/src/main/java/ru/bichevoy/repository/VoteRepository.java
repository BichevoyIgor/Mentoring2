package ru.bichevoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.User;
import ru.bichevoy.entity.Vote;
import ru.bichevoy.entity.VoteType;

import java.util.List;
import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {

    @Query("SELECT v FROM Vote v " +
            "where v.user = :user AND v.question = :question AND v.answer = :answer")
    Optional<Vote> findAnswerVote(User user,
                                  Question question,
                                  Answer answer);

    @Query("SELECT v FROM Vote v " +
            "where v.user = :user AND v.question = :question AND v.answer IS NULL")
    Optional<Vote> findQuestionVote(User user,
                                    Question question);

    @Query(value = "select vote_type, count(question_id) " +
            "from vote " +
            "where question_id = :id AND answer_id is null" +
            " group by vote_type", nativeQuery = true)
    List<Object[]> findVotesForQuestion(Long id);

    @Query(value = "select answer_id, vote_type, count(answer_id) " +
            "from vote " +
            "where question_id = :questionId AND answer_id is not null" +
            " group by answer_id, vote_type", nativeQuery = true)
    List<Object[]> findAnswerVotesForQuestion(Long questionId);

    Long countVotesByUserAndVoteType(User user, VoteType voteType);
}
