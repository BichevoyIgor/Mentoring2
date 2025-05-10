package ru.bichevoy.repository;

import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.parking.ParkingUnit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryParkingCarRepository implements ParkingCarRepository {

    private final Map<Car, ParkingUnit> repository = new HashMap<>();

    @Override
    public void addCar(Car car, ParkingUnit parkingUnit) {
        repository.put(car, parkingUnit);
    }

    @Override
    public void removeCar(Car car) {
        repository.remove(car);
    }

    @Override
    public List<Car> getAllCars() {
        return new ArrayList<>(repository.keySet());
    }

    @Override
    public ParkingUnit findParkingUnitByCar(Car car) {
        return repository.get(car);
    }
}
