package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.bichevoy.entity.Card;
import ru.bichevoy.repository.CardRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@CommonsLog
@Component
@RequiredArgsConstructor
@Slf4j
public class CardService {

    private final CardRepository cardRepository;
    private final Random random = new Random();

    public void add(Card card) {
        cardRepository.add(card);
        log.info("Add card " + card);
    }

    public void remove(int id) {
        cardRepository.remove(id);
        log.info("Remove card by id = " + id);
    }

    public List<Card> getAllCards() {
        return cardRepository.getAllCards();
    }

    public Optional<Card> getRandomCard() {
        int repositorySize = getRepositorySize();
        if (repositorySize == 0) {
            return Optional.empty();
        }
        int rand = random.nextInt(repositorySize);
        return cardRepository.findById(rand);
    }

    public int getRepositorySize() {
        return cardRepository.getRepositorySize();
    }
}
