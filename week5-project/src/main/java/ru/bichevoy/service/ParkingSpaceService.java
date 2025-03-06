package ru.bichevoy.service;

import ru.bichevoy.entity.exception.ParkingUnitException;
import ru.bichevoy.entity.parking.ParkingUnit;
import ru.bichevoy.repository.ParkingSpaceRepository;

import java.util.Set;

public class ParkingSpaceService {
    private final ParkingSpaceRepository parkingSpaceRepository;

    public ParkingSpaceService(ParkingSpaceRepository parkingSpaceRepository) {
        this.parkingSpaceRepository = parkingSpaceRepository;
    }

    public synchronized void add(ParkingUnit parkingUnit) {
        if (parkingSpaceRepository.getParkingUnitById(parkingUnit.getId()) != null) {
            throw new ParkingUnitException("Парковочное место с таким номером на парковке уже есть");
        }
        parkingSpaceRepository.add(parkingUnit);
    }

    public synchronized ParkingUnit getById(int id) {
        return parkingSpaceRepository.getParkingUnitById(id);
    }

    public synchronized Set<ParkingUnit> getAllParkingUnits() {
        return Set.copyOf(parkingSpaceRepository.getAllParkingUnits());
    }

    public synchronized void removeParkingUnit(int id) {
        parkingSpaceRepository.removeParkingUnit(id);
    }

    public synchronized int getIdForNewParkingUnit() {
        return parkingSpaceRepository.getAllParkingUnits().size() + 1;
    }

}
