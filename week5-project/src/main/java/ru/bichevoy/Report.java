package ru.bichevoy;

import lombok.Data;
import ru.bichevoy.entity.parking.ParkingSpace;

@Data
public class Report {
    private final ParkingSpace parkingSpace;

    public int getCountReject() {
        return parkingSpace.getGateIn().getCountReject();
    }

    public int getCountCarInParkingSpace() {
        return parkingSpace.getCarsInParkingSpace().size();
    }

    public int getCountFreeParkingUnits() {
        return (int) parkingSpace.getRepository().getAllParkingUnits().stream()
                .filter(unit -> unit.getCar().isEmpty())
                .count();
    }
}
