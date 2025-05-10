package ru.bichevoy.repository;

import ru.bichevoy.entity.parking.ParkingUnit;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InMemoryParkingSpaceRepository implements ParkingSpaceRepository {
    private final Map<Integer, ParkingUnit> parkingSpace;

    public InMemoryParkingSpaceRepository() {
        parkingSpace = new HashMap<>();
    }

    @Override
    public void add(ParkingUnit parkingUnit) {
        parkingSpace.put(parkingUnit.getId(), parkingUnit);
    }

    @Override
    public ParkingUnit getParkingUnitById(int id) {
        return parkingSpace.get(id);
    }


    @Override
    public Set<ParkingUnit> getAllParkingUnits() {
        return new HashSet<>(parkingSpace.values());
    }

    @Override
    public void removeParkingUnit(int id) {
        parkingSpace.remove(id);
    }
}
