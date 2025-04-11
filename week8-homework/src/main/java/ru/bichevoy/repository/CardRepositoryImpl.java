package ru.bichevoy.repository;

import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class CardRepositoryImpl implements CardRepository {

    private final List<Card> repository;

    public CardRepositoryImpl() {
        repository = new ArrayList<>();
    }

    @Override
    public void add(Card card) {
        repository.add(card);
    }

    @Override
    public void remove(int id) {
        Optional<Card> foundedCard = repository.stream()
                .filter(card -> card.getId() == id)
                .findFirst();
        foundedCard.ifPresent(repository::remove);
    }

    @Override
    public List<Card> getAllCards() {
        return repository;
    }

    @Override
    public Optional<Card> findById(int id) {
        Card card = repository.get(id);
        return Optional.of(card);
    }

    @Override
    public int getRepositorySize() {
        return repository.size();
    }
}
