package service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.parking.ParkingUnit;
import ru.bichevoy.entity.parking.PassangerParkingUnit;
import ru.bichevoy.entity.parking.TruckParkingUnit;
import ru.bichevoy.repository.InMemoryParkingSpaceRepository;
import ru.bichevoy.service.ParkingSpaceService;

import java.util.Set;

class ParkingSpaceServiceTest {
    private ParkingSpaceService repositoryService;
    private ParkingUnit passengerUnit1;
    private ParkingUnit passengerUnit2;
    private ParkingUnit truckUnit1;
    private ParkingUnit truckUnit2;

    @BeforeEach
    void init() {
        repositoryService = new ParkingSpaceService(new InMemoryParkingSpaceRepository());
        passengerUnit1 = new PassangerParkingUnit(repositoryService.getIdForNewParkingUnit());
        repositoryService.add(passengerUnit1);
        passengerUnit2 = new PassangerParkingUnit(repositoryService.getIdForNewParkingUnit());
        repositoryService.add(passengerUnit2);
        truckUnit1 = new TruckParkingUnit(repositoryService.getIdForNewParkingUnit());
        repositoryService.add(truckUnit1);
        truckUnit2 = new TruckParkingUnit(repositoryService.getIdForNewParkingUnit());
        repositoryService.add(truckUnit2);
    }

    @Test
    void add() {
        Assertions.assertEquals(4, repositoryService.getAllParkingUnits().size());
        repositoryService.add(new PassangerParkingUnit(repositoryService.getIdForNewParkingUnit()));
        Assertions.assertEquals(5, repositoryService.getAllParkingUnits().size());
    }

    @Test
    void getById() {
        ParkingUnit unit = repositoryService.getById(truckUnit1.getId());

        Assertions.assertEquals(truckUnit1, unit);
        Assertions.assertNotEquals(truckUnit2, unit);
    }

    @Test
    void getAllParkingUnit() {
        Set<ParkingUnit> parkingSet = Set.of(passengerUnit1, passengerUnit2, truckUnit1, truckUnit2);

        Assertions.assertEquals(4, parkingSet.size());
        Assertions.assertTrue(parkingSet.containsAll(repositoryService.getAllParkingUnits()));
    }

    @Test
    void removeParkingUnit() {

        repositoryService.removeParkingUnit(passengerUnit2.getId());
        Set<ParkingUnit> parkingSet = Set.of(passengerUnit1, truckUnit1, truckUnit2);

        Assertions.assertTrue(parkingSet.containsAll(repositoryService.getAllParkingUnits()));
    }
}