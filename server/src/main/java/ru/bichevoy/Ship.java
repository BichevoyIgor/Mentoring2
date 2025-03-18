package ru.bichevoy;

import lombok.Data;

@Data
public class Ship {
    private String[] decks;
    private boolean isAlive;

    Ship(int countDecks) {
        decks = new String[countDecks];
        isAlive = true;
    }

    public boolean isInit() {
        for (String deck : decks) {
            if (deck == null) {
                return true;
            }
        }
        return false;
    }
}
