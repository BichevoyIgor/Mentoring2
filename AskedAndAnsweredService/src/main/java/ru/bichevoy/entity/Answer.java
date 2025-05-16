package ru.bichevoy.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDate date;
    @ManyToOne
    private User user;
    @ManyToOne
    private Question question;

    public Answer() {
    }

    public Answer(String content, LocalDate date, User user, Question question) {
        this.content = content;
        this.date = date;
        this.user = user;
        this.question = question;
    }
}
