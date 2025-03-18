package ru.bichevoy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameServiceTest {
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameService();
    }

    /**
     *    A Б В Г Д Е Ж З И К
     * 1  . . . . . . . . . .
     * 2  . . . . . . . . . .
     * 3  . . . . . . . . . .
     * 4  . . . . . . . . . .
     * 5  . . . . . . . . . .
     * 6  . . . . . . . . . .
     * 7  . . . . . . . . . .
     * 8  . . . . . . . . . .
     * 9  . . . . . . . . . .
     * 10 . . . . . . . . . .
     */

    @Test
    void validateCoordinates() {
        Assertions.assertTrue(gameService.validateCoordinates("А1, А2, А3"));
        Assertions.assertTrue(gameService.validateCoordinates("А1,А2,А3"));
        Assertions.assertTrue(gameService.validateCoordinates("а1,а2,а3"));
        Assertions.assertTrue(gameService.validateCoordinates("а1,а2"));
        Assertions.assertFalse(gameService.validateCoordinates("А1, А2, Б3"));
        Assertions.assertFalse(gameService.validateCoordinates("А1, А2, Б2"));
        Assertions.assertFalse(gameService.validateCoordinates("А1, В2, А2"));
    }
}
