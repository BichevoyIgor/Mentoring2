package ru.bichevoy.repository;

import ru.bichevoy.entity.Card;

import java.util.List;
import java.util.Optional;

public interface CardRepository {
    void add(Card card);

    void remove(int id);

    List<Card> getAllCards();

    Optional<Card> findById(int id);

    int getRepositorySize();
}
