package ru.bichevoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bichevoy.entity.Answer;
import ru.bichevoy.entity.User;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionId(Long id);

    Optional<Answer> findAnswerById(Long id);

    long countAnswerByUser(User user);
}
