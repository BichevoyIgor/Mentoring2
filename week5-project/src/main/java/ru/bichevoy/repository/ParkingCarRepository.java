package ru.bichevoy.repository;

import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.parking.ParkingUnit;

import java.util.List;

public interface ParkingCarRepository {
    void addCar(Car car, ParkingUnit parkingUnit);

    void removeCar(Car car);

    List<Car> getAllCars();

    ParkingUnit findParkingUnitByCar(Car car);
}
