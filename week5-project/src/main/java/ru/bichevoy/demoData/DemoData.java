package ru.bichevoy.demoData;

import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.car.PassengerCar;
import ru.bichevoy.entity.car.Truck;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

public class DemoData {
    private final static Random random = new Random();

    /**
     * Генерация списка автомобилей
     */
    public static CopyOnWriteArrayList<Car> generateCars(int countPassengerCar, int countTruck) {
        Set<Car> carSet = new HashSet<>();

        for (int i = 0; i < countPassengerCar; i++) {
            carSet.add(new PassengerCar(getNumber()));
        }

        for (int i = 0; i < countTruck; i++) {
            carSet.add(new Truck(getNumber()));
        }

        return new CopyOnWriteArrayList<>(carSet);
    }

    /**
     * Генерация номеров авто
     *
     * @return Гос номер авто
     */
    private static String getNumber() {
        String[] letters = {"А", "В", "Е", "К", "М", "Н", "Р", "С", "Т", "У", "Х"};
        int num = random.nextInt((999 - 100) + 1) + 100;
        String number = String.format("%s%d%s%s%d%dRUS",
                letters[random.nextInt(letters.length)],
                num,
                letters[random.nextInt(letters.length)],
                letters[random.nextInt(letters.length)],
                random.nextInt((9 - 1) + 1) + 1,
                random.nextInt((9 - 1) + 1) + 1
        );
        return number;
    }
}
