package ru.bichevoy.repository;

import ru.bichevoy.entity.parking.ParkingUnit;

import java.util.Set;

public interface ParkingSpaceRepository {
    void add(ParkingUnit parkingUnit);

    ParkingUnit getParkingUnitById(int id);

    Set<ParkingUnit> getAllParkingUnits();

    void removeParkingUnit(int id);
}
