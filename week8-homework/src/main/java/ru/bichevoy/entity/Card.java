package ru.bichevoy.entity;

import lombok.Data;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class Card {
    private static AtomicInteger idCounter = new AtomicInteger();

    private int id;
    private final String word;
    private final Set<String> translate;

    public Card(String word, Set<String> translate) {
        this.id = idCounter.incrementAndGet();
        this.word = word;
        this.translate = translate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("%d. %s - %s",
                id,
                word,
                String.join(", ", translate));
    }
}
