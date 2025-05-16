package ru.bichevoy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private VoteType voteType;
    @ManyToOne
    private User user;
    @ManyToOne
    private Question question;
    @ManyToOne
    private Answer answer;

    public Vote(VoteType voteType, User user, Question question, Answer answer) {
        this.voteType = voteType;
        this.user = user;
        this.question = question;
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return Objects.equals(user, vote.user) && Objects.equals(question, vote.question) && Objects.equals(answer, vote.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, question, answer);
    }
}
