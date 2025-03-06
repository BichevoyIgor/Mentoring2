package ru.bichevoy.entity;

import lombok.Data;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.parking.ParkingUnit;

import java.time.LocalDateTime;

@Data
public class VisitHistory {

    private final LocalDateTime start;
    private final Car car;
    private final ParkingUnit parkingUnit;
    private LocalDateTime end;
}
