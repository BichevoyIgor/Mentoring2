package ru.bichevoy.entity.parking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.car.PassengerCar;
import ru.bichevoy.entity.exception.ParkingUnitException;

import java.util.ArrayList;
import java.util.List;

/**
 * Парковочное место для легкового авто
 */
@Data
@Slf4j
public class PassangerParkingUnit extends ParkingUnit {

    private final String UNIT_BUSY = "Парковочное место занято ";
    private final String UNIT_CAR_ERROR = "Парковочное место предназначено для легковой машины";

    public PassangerParkingUnit(int id) {
        super(id);
    }

    /**
     * Покинуть парковочное место
     *
     * @param car
     */
    @Override
    public void removeCar(Car car) {
        if (car.equals(super.car)) {
            super.car = null;
        }
    }

    /**
     * Получить авто которое припарковано на месте
     *
     * @return
     */
    @Override
    public List<Car> getCar() {
        List<Car> car = new ArrayList<>();
        if (super.car != null) {
            car.add(super.car);
        }
        return car;
    }

    /**
     * Припарковать авто на парковочное место
     *
     * @param car
     */
    @Override
    public void setCar(Car car) {
        if (super.car != null) {
            throw new ParkingUnitException(UNIT_BUSY + super.car.getRegNomer());
        }
        if (car instanceof PassengerCar) {
            super.car = car;
        } else {
            throw new ParkingUnitException(UNIT_CAR_ERROR);
        }
    }

    @Override
    public String toString() {
        return "PassangerParkingUnit{" +
                "id=" + id +
                ", car=" + car +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
