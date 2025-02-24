package ru.bichevoy.Entity;

import lombok.Data;

import java.util.*;

@Data
public class Civilization {
    private final String name;
    private volatile Map<String, Colony> colonyMap = new LinkedHashMap<>();

    public void addColony(int x, int y) {
        synchronized (colonyMap) {
            Colony c = new Colony(x, y);
            colonyMap.put(String.format("%d,%d", x, y), c);
        }
    }

    public Colony getColony(int x, int y) {
        return colonyMap.get(String.format("%d,%d", x, y));
    }

    public void addColonyPower(int i, int j, double power) {
        synchronized (colonyMap) {
            Colony colony = getColony(i, j);
            if (colony.power == 10_000) {
                givePowerAnotherColony(power);
            } else {
                colony.power += power;
                if (colony.power > 10_000) {
                    double remains = colony.power - 10_000;
                    colony.power = 10_000;
                    givePowerAnotherColony(remains);
                }
            }
        }
    }

    private void givePowerAnotherColony(double power) {
        List<String> keySet = new ArrayList<>(colonyMap.keySet());
        Collections.reverse(keySet);
        for (int i = 0; i < keySet.size(); i++) {
            Colony colony = colonyMap.get(keySet.get(i));
            if (colony.power < 10_000) {
                colony.power += power;
                if (colony.power > 10_000) {
                    double remains = colony.power - 10_000;
                    colony.power = 10_000;
                    givePowerAnotherColony(remains);
                }
            }
        }
    }

    public void removeColony(int x, int y) {
        synchronized (colonyMap) {
            colonyMap.remove(String.format("%d,%d", x, y));
        }
    }

    @Data
    public class Colony {
        private final int x;
        private final int y;
        private double power = 10;
    }
}


