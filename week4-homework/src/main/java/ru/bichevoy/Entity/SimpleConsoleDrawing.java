package ru.bichevoy.Entity;

import lombok.Data;
import lombok.NonNull;

@Data
public class SimpleConsoleDrawing implements Drawable {

    public static final String CLEAR_SCREEN = "\033[H\033[2J";
    public static final String RESET = "\033[0m";
    public static final String RED = "\033[31m";
    public static final String GREEN = "\033[32m";
    public static final String BLUE = "\033[34m";
    public static final String YELLOW = "\033[33m";
    public static final String WHITE = "\033[37m";
    @NonNull
    private final String[][] map;

    @Override
    public synchronized void draw() {
        clearConsole();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                switch (map[i][j]) {
                    case "A":
                        System.out.print(RED + "A" + RESET);
                        break;
                    case "B":
                        System.out.print(GREEN + "B" + RESET);
                        break;
                    case "C":
                        System.out.print(BLUE + "C" + RESET);
                        break;
                    case "D":
                        System.out.print(YELLOW + "D" + RESET);
                        break;
                    case "1":
                        System.out.print(WHITE + "█" + RESET);
                        break;
                    default:
                        System.out.print(" ");
                        break;
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void clearConsole() {
        System.out.print(CLEAR_SCREEN);
        System.out.flush();
    }
}
