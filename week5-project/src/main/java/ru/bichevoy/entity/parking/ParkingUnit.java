package ru.bichevoy.entity.parking;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import ru.bichevoy.entity.car.Car;

import java.util.List;

/**
 * Парковочное место
 */
@Data
@EqualsAndHashCode(exclude = "car")
public abstract class ParkingUnit {
    @NonNull
    protected int id;
    protected Car car;

    public ParkingUnit() {
    }

    public ParkingUnit(@NonNull int id) {
        this.id = id;
    }

    public abstract List<Car> getCar();

    /**
     * Припарковать авто на парковочное место
     *
     * @param car
     */
    public abstract void setCar(Car car);

    /**
     * Освободить парковочное место
     *
     * @param car
     */
    public abstract void removeCar(Car car);

}
