package ru.bichevoy.entity.parking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.car.PassengerCar;
import ru.bichevoy.entity.car.Truck;
import ru.bichevoy.entity.exception.ParkingUnitException;
import ru.bichevoy.repository.InMemoryParkingSpaceRepository;
import ru.bichevoy.service.ParkingSpaceService;

import static org.junit.jupiter.api.Assertions.*;

class TruckParkingUnitTest {
    private Car passengerCar1;
    private Car passengerCar2;
    private Car passengerCar3;
    private Car truck;
    private Car truck2;
    private ParkingUnit parkingUnit;
    private ParkingSpaceService repositoryService;

    @BeforeEach
    void init() {
        repositoryService = new ParkingSpaceService(new InMemoryParkingSpaceRepository());
        passengerCar1 = new PassengerCar("L001LL");
        passengerCar2 = new PassengerCar("L002LL");
        passengerCar3 = new PassengerCar("L003LL");
        truck = new Truck("T001TT");
        truck2 = new Truck("T002TT");
        parkingUnit = new TruckParkingUnit(repositoryService.getIdForNewParkingUnit());
    }

    @Test
    void setPassengerCar() {
        assertDoesNotThrow(() -> parkingUnit.setCar(passengerCar2));
    }

    @Test
    void set2PassengerCars() {
        parkingUnit.setCar(passengerCar1);
        assertDoesNotThrow(() -> parkingUnit.setCar(passengerCar2));
    }

    @Test
    void set3PassengerCarsThrowTest() {
        parkingUnit.setCar(passengerCar1);
        parkingUnit.setCar(passengerCar2);
        assertThrows(ParkingUnitException.class, () -> parkingUnit.setCar(passengerCar3));
    }

    @Test
    void set2PassengerCarsAndTruckThrowTest() {
        parkingUnit.setCar(passengerCar1);
        parkingUnit.setCar(passengerCar2);
        assertThrows(ParkingUnitException.class, () -> parkingUnit.setCar(truck));
    }

    @Test
    void setPassengerCarAndTruckThrowTest() {
        parkingUnit.setCar(passengerCar1);
        assertThrows(ParkingUnitException.class, () -> parkingUnit.setCar(truck));
    }

    @Test
    void setTruck() {
        assertDoesNotThrow(() -> parkingUnit.setCar(truck));
    }

    @Test
    void set2TruckThrowTest() {
        parkingUnit.setCar(truck);
        assertThrows(ParkingUnitException.class, () -> parkingUnit.setCar(truck2));
    }

    @Test
    void setTruckAndPassengerCarThrowTest() {
        parkingUnit.setCar(truck);
        assertThrows(ParkingUnitException.class, () -> parkingUnit.setCar(passengerCar1));
    }

    @Test
    void checkSetCar() {
        parkingUnit.setCar(truck);
        Car car = (parkingUnit.getCar()).get(0);
        assertEquals(truck, car);
    }

    @Test
    void setCarGetSize() {
        parkingUnit.setCar(passengerCar1);
        parkingUnit.setCar(passengerCar2);
        assertEquals(2, parkingUnit.getCar().size());
        assertNotEquals(1, parkingUnit.getCar().size());
    }

    @Test
    void removeCar() {
        parkingUnit.setCar(passengerCar1);
        parkingUnit.setCar(passengerCar2);
        assertEquals(2, parkingUnit.getCar().size());
        parkingUnit.removeCar(passengerCar1);
        assertEquals(1, parkingUnit.getCar().size());
        parkingUnit.removeCar(passengerCar2);
        assertTrue(parkingUnit.getCar().isEmpty());
    }
}