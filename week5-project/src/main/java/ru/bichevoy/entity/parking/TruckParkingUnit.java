package ru.bichevoy.entity.parking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.car.PassengerCar;
import ru.bichevoy.entity.car.Truck;
import ru.bichevoy.entity.exception.ParkingUnitException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Парковочное место для грузового авто
 */
@Data
@Slf4j
public class TruckParkingUnit extends ParkingUnit {

    private final String UNIT_BUSY = "Парковочное место занято ";
    private final Set<PassengerCar> passengerCars = new HashSet<>(2);

    public TruckParkingUnit(int id) {
        super(id);
    }

    /**
     * Покинуть парковочное место
     *
     * @param car
     */
    public void removeCar(Car car) {
        if (car.equals(super.car)) {
            super.car = null;
        }
        passengerCars.remove(car);
    }

    /**
     * Проверка, занято ли парковочное место грузовиком
     *
     * @return
     */
    public boolean truckSpaceIsEmpty() {
        return super.car == null;
    }

    /**
     * Проверка, можно ли припарковать легковое авто
     *
     * @return
     */
    public int countFreeSpaceForPassengerCar() {
        if (truckSpaceIsEmpty()) {
            return 2 - passengerCars.size();
        }
        return 0;
    }

    /**
     * Получить список припаркованных авто
     *
     * @return если припаркован грузовик, вернется list.size() == 1, если припаркованы легковые авто, вернется список
     */
    @Override
    public List<Car> getCar() {
        if (!passengerCars.isEmpty()) {
            return new ArrayList<>(passengerCars);
        } else {
            List<Car> car = new ArrayList<>();
            if (super.car != null) {
                car.add(super.car);
            }
            return car;
        }
    }

    /**
     * Припарковать грузовое авто.
     * Парковочное место может вместить 1 грузовик или 2 легковых авто.
     *
     * @param car
     */
    public void setCar(Car car) {
        if (super.car != null) {
            throw new ParkingUnitException(UNIT_BUSY + super.car.getRegNomer());
        }

        if (car instanceof Truck) {
            if (!passengerCars.isEmpty()) {
                throw new ParkingUnitException(String.format("%s: %s",
                        UNIT_BUSY,
                        passengerCars.stream()
                                .map(c -> c.getRegNomer())
                                .collect(Collectors.joining(" ,"))));
            }
            super.car = car;
        } else if (passengerCars.size() == 2) {
            String message = "Парковка занята двумя легковушками";
            log.info(message);
            throw new ParkingUnitException(message);
        } else {
            passengerCars.add((PassengerCar) car);
        }
    }

    @Override
    public String toString() {
        return "TruckParkingUnit{" +
                "id=" + id +
                ", passengerCars=" + passengerCars +
                ", truck=" + car +
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
