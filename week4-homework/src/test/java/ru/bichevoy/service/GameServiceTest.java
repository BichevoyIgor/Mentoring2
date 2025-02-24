package ru.bichevoy.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.bichevoy.Entity.Civilization;
import ru.bichevoy.Entity.Drawable;
import ru.bichevoy.Entity.SimpleConsoleDrawing;

import java.util.Arrays;
import java.util.stream.Collectors;

class GameServiceTest {

    @Test
    void playerGrowTest() {
        String[][] map = {
                {"A", "0", "0", "0", "1", "1", "A"},
                {"1", "1", "0", "0", "0", "1", "0"},
                {"0", "0", "0", "A", "0", "0", "0"},
                {"0", "0", "0", "0", "1", "0", "0"},
                {"1", "0", "0", "0", "0", "0", "1"},
                {"1", "1", "1", "1", "0", "A", "0"},
        };
        String[][] mapForAssert = {
                {"A", "T", "*", "*", "*", "T", "A"},
                {"T", "T", "T", "T", "T", "T", "T"},
                {"*", "*", "T", "A", "T", "*", "*"},
                {"*", "*", "T", "T", "T", "*", "*"},
                {"*", "*", "*", "*", "T", "T", "T"},
                {"*", "*", "*", "*", "T", "A", "T"},
        };
        String stringViewMap = Arrays.stream(mapForAssert)
                .map(s -> Arrays.stream(s).collect(Collectors.joining()))
                .collect(Collectors.joining());

        Civilization civilization1 = new Civilization("A");
        Drawable drawable = new SimpleConsoleDrawing(map);
        GameService gameService = new GameService(500, map, civilization1, new Civilization("D"), drawable);
        String[][] playerMap = gameService.getPlayerMap(map, civilization1);
        gameService.playerGrow(playerMap, civilization1);

        String stringViewPlayerMap = Arrays.stream(playerMap)
                .map(s -> Arrays.stream(s).collect(Collectors.joining()))
                .collect(Collectors.joining());

        Assertions.assertEquals(stringViewPlayerMap, stringViewMap);
    }

    @Test
    void concatMapsTest() {
        String[][] map = {
                {"A", "0", "0", "0", "0", "0", "1", "1", "1", "1"},
                {"1", "1", "0", "0", "0", "0", "0", "0", "1", "0"},
                {"0", "0", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "1", "0", "0", "0", "0", "1", "1", "1"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "1", "0"},
                {"0", "1", "1", "1", "1", "0", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "0", "0"},
                {"1", "0", "0", "0", "0", "0", "0", "0", "0", "1"},
                {"1", "1", "1", "1", "0", "0", "0", "0", "D", "0"},
        };
        Civilization player1 = new Civilization("A");
        Civilization player2 = new Civilization("D");
        Drawable drawable = new SimpleConsoleDrawing(map);
        GameService gameService = new GameService(500, map, player1, player2, drawable);

        String[][] playerMap = gameService.getPlayerMap(map, player1);
        gameService.playerGrow(playerMap, player1);
        gameService.concatMaps(playerMap, player1);

        String[][] player2Map = gameService.getPlayerMap(map, player2);
        gameService.playerGrow(player2Map, player2);
        gameService.concatMaps(player2Map, player2);


        String[][] mapForAssert = {
                {"A", "A", "0", "0", "0", "0", "1", "1", "1", "1"},
                {"1", "1", "0", "0", "0", "0", "0", "0", "1", "0"},
                {"0", "0", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "1", "0", "0", "0", "0", "1", "1", "1"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "1", "0"},
                {"0", "1", "1", "1", "1", "0", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "0", "0"},
                {"1", "0", "0", "0", "0", "0", "0", "D", "D", "1"},
                {"1", "1", "1", "1", "0", "0", "0", "D", "D", "D"},
        };
        String stringViewMapForAssert = Arrays.stream(mapForAssert)
                .map(s -> Arrays.stream(s).collect(Collectors.joining()))
                .collect(Collectors.joining());

        String stringViewMap = Arrays.stream(map)
                .map(s -> Arrays.stream(s).collect(Collectors.joining()))
                .collect(Collectors.joining());

        Assertions.assertEquals(stringViewMapForAssert, stringViewMap);
    }

    @Test
    void gameLoopThreeIter() {
        String[][] map = {
                {"A", "0", "0", "0", "0", "0", "1", "1", "1", "1"},
                {"1", "1", "0", "0", "0", "0", "0", "0", "1", "0"},
                {"0", "0", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "1", "0", "0", "0", "0", "1", "1", "1"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "1", "0"},
                {"0", "1", "1", "1", "1", "0", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "0", "0"},
                {"1", "0", "0", "0", "0", "0", "0", "0", "0", "1"},
                {"1", "1", "1", "1", "0", "0", "0", "0", "D", "0"},
        };
        Civilization player1 = new Civilization("A");
        Civilization player2 = new Civilization("D");
        Drawable drawable = new SimpleConsoleDrawing(map);
        GameService gameService = new GameService(500, map, player1, player2, drawable);

        for (int i = 0; i < 3; i++) {
            String[][] playerMap = gameService.getPlayerMap(map, player1);
            gameService.playerGrow(playerMap, player1);
            gameService.concatMaps(playerMap, player1);

            String[][] player2Map = gameService.getPlayerMap(map, player2);
            gameService.playerGrow(player2Map, player2);
            gameService.concatMaps(player2Map, player2);
        }

        String[][] mapForAssert = {
                {"A", "A", "A", "A", "0", "0", "1", "1", "1", "1"},
                {"1", "1", "A", "A", "0", "0", "0", "0", "1", "0"},
                {"0", "A", "A", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "1", "0", "0", "0", "0", "1", "1", "1"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "1", "0", "1", "1", "1", "0", "0", "0", "0"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "1", "0"},
                {"0", "1", "1", "1", "1", "0", "0", "D", "D", "D"},
                {"0", "0", "0", "0", "0", "1", "1", "1", "D", "D"},
                {"1", "0", "0", "0", "0", "D", "D", "D", "D", "1"},
                {"1", "1", "1", "1", "0", "D", "D", "D", "D", "D"},
        };
        String stringViewMapForAssert = Arrays.stream(mapForAssert)
                .map(s -> Arrays.stream(s).collect(Collectors.joining()))
                .collect(Collectors.joining());

        String stringViewMap = Arrays.stream(map)
                .map(s -> Arrays.stream(s).collect(Collectors.joining()))
                .collect(Collectors.joining());

        Assertions.assertEquals(stringViewMapForAssert, stringViewMap);
    }
}