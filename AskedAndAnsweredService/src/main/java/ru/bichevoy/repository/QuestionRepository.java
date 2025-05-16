package ru.bichevoy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.bichevoy.entity.Question;
import ru.bichevoy.entity.User;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    @Query("select q from Question q join q.tags t where t.name = :tagName")
    List<Question> findAllByTagName(String tagName);

    List<Question> findByHeaderContainingIgnoreCase(String question);

    Long countQuestionByAuthor(User user);
}
