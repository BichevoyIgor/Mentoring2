package ru.bichevoy.service;

import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.parking.ParkingUnit;
import ru.bichevoy.repository.ParkingCarRepository;

import java.util.List;

public class ParkingCarService {
    private final ParkingCarRepository repository;

    public ParkingCarService(ParkingCarRepository repository) {
        this.repository = repository;
    }

    public synchronized void addCar(Car car, ParkingUnit parkingUnit) {
        repository.addCar(car, parkingUnit);
    }

    public synchronized void removeCar(Car car) {
        repository.removeCar(car);
    }

    public synchronized List<Car> getAllCars() {
        return repository.getAllCars();
    }

    public synchronized ParkingUnit findParkingUnitByCar(Car car) {
        return repository.findParkingUnitByCar(car);
    }
}
