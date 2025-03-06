package ru.bichevoy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.parking.ParkingSpace;
import ru.bichevoy.entity.parking.ParkingUnit;

import java.util.Set;

class ParkingProcessTest {

    private ParkingSpace parkingSpace;

    @BeforeEach
    void setUp() {
        parkingSpace = new ParkingSpace();
    }

    @Test
    void initParkingUnitsFromDataPropertiesTest() {
        Set<ParkingUnit> allParkingUnits = parkingSpace.getRepository().getAllParkingUnits();
        int countPassengerParkingUnit = Integer.parseInt(ProgramProperties.getProperties().getProperty("countPassengerParkingUnit"));
        int countTruckParkingUnit = Integer.parseInt(ProgramProperties.getProperties().getProperty("countTruckParkingUnit"));
        Assertions.assertTrue(allParkingUnits.size() == (countPassengerParkingUnit + countTruckParkingUnit));
    }


}