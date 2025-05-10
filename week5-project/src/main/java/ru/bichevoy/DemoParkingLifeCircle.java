package ru.bichevoy;

import ru.bichevoy.demoData.DemoData;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.parking.ParkingSpace;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DemoParkingLifeCircle {

    public void start() {
        CopyOnWriteArrayList<Car> cars = DemoData.generateCars(100, 50);
        Collections.shuffle(cars);

        ParkingSpace parkingSpace = new ParkingSpace();

        /**
         * Автомобили по таймеру подъезжают к воротам
         */
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(() -> {
            if (!parkingSpace.isOpen()) {
                Thread.currentThread().interrupt();
            }
            if (cars.size() > 0 && !Thread.currentThread().isInterrupted()) {
                parkingSpace.getGateIn().moveIn(cars.removeFirst());
            }
        }, 2, 2, TimeUnit.SECONDS);

        /**
         * Замыкаем цикл, выезжающие авто с парковки, попадают в общий список автомобилей
         */
        new Thread(() -> {
            while (parkingSpace.isOpen() || parkingSpace.getCountCarsInParkingSpace() > 0) {
                if (parkingSpace.getGateOut().countCarInOutGateQueue() > 0) {
                    Car car = parkingSpace.getGateOut().moveOut();
                    cars.add(car);
                }
            }
        }).start();

        /**
         * Забираем авто с парковки
         */
        new Thread(() -> {
            Random random = new Random();
            while (parkingSpace.isOpen() || parkingSpace.getCountCarsInParkingSpace() > 0) {
                List<Car> carsInParkingSpace = parkingSpace.getCarsInParkingSpace();
                if (parkingSpace.getCountCarsInParkingSpace() > 0) {
                    Car car = carsInParkingSpace.get(random.nextInt(parkingSpace.getCountCarsInParkingSpace()));
                    System.out.println("---ZABIRAEM MASHINU " + car.getRegNomer() + "---");
                    parkingSpace.unParking(car);
                }
                try {
                    Thread.sleep(random.nextInt(10000));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        try {
            Thread.sleep(100000);
            System.out.println("--------Zakrivaem stoianky------");
            parkingSpace.setOpen(false);
            scheduledExecutorService.shutdown();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
