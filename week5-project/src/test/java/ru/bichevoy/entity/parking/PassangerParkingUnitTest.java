package ru.bichevoy.entity.parking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.car.PassengerCar;
import ru.bichevoy.entity.car.Truck;
import ru.bichevoy.entity.exception.ParkingUnitException;
import ru.bichevoy.repository.InMemoryParkingSpaceRepository;
import ru.bichevoy.service.ParkingSpaceService;

import java.util.List;

class PassangerParkingUnitTest {
    private Car passengerCar1;
    private Car passengerCar2;
    private Car passengerCar3;
    private Car truck;
    private ParkingUnit parkingUnit;
    private ParkingSpaceService repositoryService;

    @BeforeEach
    void init() {
        repositoryService = new ParkingSpaceService(new InMemoryParkingSpaceRepository());
        passengerCar1 = new PassengerCar("L001LL");
        passengerCar2 = new PassengerCar("L002LL");
        passengerCar3 = new PassengerCar("L003LL");
        truck = new Truck("T001TT");
        parkingUnit = new PassangerParkingUnit(repositoryService.getIdForNewParkingUnit());
    }

    @Test
    void setCar() {
        parkingUnit.setCar(passengerCar1);
        List<Car> car = parkingUnit.getCar();
        Assertions.assertEquals(1, car.size());
    }

    @Test
    void set2CarsThrowTest() {
        parkingUnit.setCar(passengerCar1);
        Assertions.assertThrows(ParkingUnitException.class, () -> parkingUnit.setCar(passengerCar2));
    }

    @Test
    void setCarTruckThrowTest() {
        Assertions.assertThrows(ParkingUnitException.class, () -> parkingUnit.setCar(truck));
    }

    @Test
    void removeCar() {
        parkingUnit.setCar(passengerCar1);
        List<Car> car = parkingUnit.getCar();
        parkingUnit.removeCar(car.get(0));
        Assertions.assertTrue(parkingUnit.getCar().isEmpty());
    }

    @Test
    void getCar() {
        parkingUnit.setCar(passengerCar1);
        List<Car> car = parkingUnit.getCar();
        Assertions.assertEquals(passengerCar1, car.get(0));
        Assertions.assertNotEquals(passengerCar2, car.get(0));
    }
}