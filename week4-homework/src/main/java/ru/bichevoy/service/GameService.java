package ru.bichevoy.service;

import lombok.Data;
import lombok.NonNull;
import ru.bichevoy.Entity.Civilization;
import ru.bichevoy.Entity.Drawable;
import ru.bichevoy.WinnerFoundException;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Data
public class GameService {

    @NonNull
    private final int speed;
    @NonNull
    private volatile String[][] globalMap;
    @NonNull
    private Civilization player1;
    @NonNull
    private Civilization player2;
    @NonNull
    private Drawable drawable;

    private Thread threadPlayer1;
    private Thread threadPlayer2;

    public void startFight() {
        AtomicReference<String> winnerName = new AtomicReference<>();
        threadPlayer1 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    gameLoop(player1);
                } catch (InterruptedException e) {
                    return;
                } catch (WinnerFoundException e) {
                    winnerName.set(e.getMessage());
                    threadPlayer1.interrupt();
                    threadPlayer2.interrupt();
                    return;
                }
            }
        });

        threadPlayer2 = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    gameLoop(player2);
                } catch (InterruptedException e) {
                    return;
                } catch (WinnerFoundException e) {
                    winnerName.set(e.getMessage());
                    threadPlayer1.interrupt();
                    threadPlayer2.interrupt();
                    return;
                }
            }
        });
        setStartPosition(player1);
        setStartPosition(player2);
        threadPlayer1.start();
        threadPlayer2.start();
        try {
            threadPlayer1.join();
            threadPlayer2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        drawable.draw();
        System.out.printf("Winner %s\nThe End", winnerName.get());
    }

    void gameLoop(Civilization player) throws InterruptedException, WinnerFoundException {
        String[][] playerMap = getPlayerMap(globalMap, player);
        playerGrow(playerMap, player);
        concatMaps(playerMap, player);
        Optional<Civilization> winPlayer = checkWin();
        if (winPlayer.isPresent()) {
            String[][] playerMap1 = getPlayerMap(globalMap, player);
            playerGrow(playerMap1, player);
            concatMaps(playerMap1, player);
            throw new WinnerFoundException(winPlayer.get().getName());
        }
        drawable.draw();
        spreadEnergy(player);
        Thread.sleep(speed);
    }

    String[][] getPlayerMap(String[][] map, Civilization player) {
        String[][] mapPlayer = new String[map.length][];
        for (int i = 0; i < map.length; i++) {
            mapPlayer[i] = new String[map[i].length];
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j].equals(player.getName())) {
                    mapPlayer[i][j] = player.getName();
                } else {
                    mapPlayer[i][j] = "*";
                }
            }
        }
        return mapPlayer;
    }

    void playerGrow(String[][] playerMap, Civilization player) {
        for (int i0 = 0; i0 < playerMap.length; i0++) {
            for (int j0 = 0; j0 < playerMap[i0].length; j0++) {
                if (playerMap[i0][j0] != null && playerMap[i0][j0].equals(player.getName())) {
                    int height = playerMap.length;
                    int width = playerMap[0].length;
                    for (int i = i0 - 1; i <= i0 + 1; ++i) {
                        for (int j = j0 - 1; j <= j0 + 1; ++j) {
                            if (0 <= i && i < height
                                    && 0 <= j && j < width
                                    && (i != i0 || j != j0)
                            ) {
                                // операция с соседним элементом
                                if (!playerMap[i][j].equals(player.getName())) {
                                    playerMap[i][j] = "T";
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void concatMaps(String[][] playerMap, Civilization player) {
        synchronized (globalMap) {
            for (int i = 0; i < globalMap.length; i++) {
                for (int j = 0; j < globalMap[i].length; j++) {
                    if (globalMap[i][j].equals("0") && playerMap[i][j].equals("T")) {
                        globalMap[i][j] = player.getName();
                        player.addColony(i, j);
                    }

                    String opponentName = player.getName().equals(player1.getName()) ? player2.getName() : player1.getName();

                    if (globalMap[i][j].equals(opponentName) && playerMap[i][j].equals("T")) {
                        player.addColony(i, j);
                        Civilization.Colony player1Colony = player1.getColony(i, j);
                        Civilization.Colony player2Colony = player2.getColony(i, j);

                        if (player2Colony.getPower() > player1Colony.getPower() * 1.5) {
                            player2Colony.setPower(player2Colony.getPower() - player1Colony.getPower() * 1.5);
                            player1.removeColony(i, j);
                            player2.addColony(i, j);
                            globalMap[i][j] = player2.getName();
                        } else if (player1Colony.getPower() > player2Colony.getPower() * 1.5) {
                            player1Colony.setPower(player1Colony.getPower() - player2Colony.getPower() * 1.5);
                            player2.removeColony(i, j);
                            player1.addColony(i, j);
                            globalMap[i][j] = player1.getName();
                        } else if (player1Colony.getPower() == player2Colony.getPower()) {
                            player1.removeColony(i, j);
                            player2.removeColony(i, j);
                            globalMap[i][j] = "0";
                        }
                    }
                }
            }
        }
    }

    Optional<Civilization> checkWin() {
        Optional<Civilization> winner = Optional.empty();
        String mapStringView = Arrays.stream(globalMap)
                .map(s -> Arrays.stream(s).collect(Collectors.joining()))
                .collect(Collectors.joining());
        if (!mapStringView.contains(player1.getName())) {
            winner = Optional.of(player2);
        }
        if (!mapStringView.contains(player2.getName())) {
            winner = Optional.of(player1);
        }
        return winner;
    }


    void spreadEnergy(Civilization player) {
        synchronized (globalMap) {
            for (int i = 0; i < globalMap.length; i++) {
                for (int j = 0; j < globalMap[i].length; j++) {
                    if (globalMap[i][j].equals(player.getName())) {
                        int countColonyAroundCell = getCountColonyAroundCell(i, j, player.getName());
                        player.addColonyPower(i, j, countColonyAroundCell);
                    }
                }
            }
        }
    }

    private int getCountColonyAroundCell(int i0, int j0, String civilizationName) {
        int power = 0;
        int height = globalMap.length;
        int width = globalMap[0].length;
        for (int i = i0 - 1; i <= i0 + 1; ++i) {
            for (int j = j0 - 1; j <= j0 + 1; ++j) {
                // проверка на выход за границы массива
                // и проверка на то, что обрабатываемая ячейка не равна изначальной ячейке
                if (0 <= i && i < height && 0 <= j && j < width && (i != i0 || j != j0)) {
                    // любая операция с соседним элементом
                    if (globalMap[i][j].equals(civilizationName)) {
                        power++;
                    }
                }
            }
        }
        return power;
    }

    private void setStartPosition(Civilization civilization) {
        for (int i = 0; i < globalMap.length; i++) {
            for (int j = 0; j < globalMap[i].length; j++) {
                if (globalMap[i][j].equals(civilization.getName())) {
                    civilization.addColony(i, j);
                }
            }
        }
    }
}



