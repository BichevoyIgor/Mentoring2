package ru.bichevoy;

import ru.bichevoy.Entity.Civilization;
import ru.bichevoy.Entity.SimpleConsoleDrawing;
import ru.bichevoy.service.GameService;

public class Game {
    public static void main(String[] args) {
        String[][] map = {
                {"A", "0", "0", "0", "0", "0", "0", "0", "1", "1"},
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

        Civilization civilization1 = new Civilization("A");
        Civilization civilization2 = new Civilization("D");
        SimpleConsoleDrawing simpleConsoleDrawing = new SimpleConsoleDrawing(map);
        GameService gameService = new GameService(5, map, civilization1, civilization2, simpleConsoleDrawing);
        gameService.startFight();
    }
}
