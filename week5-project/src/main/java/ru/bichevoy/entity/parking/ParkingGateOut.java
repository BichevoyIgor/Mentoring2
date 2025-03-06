package ru.bichevoy.entity.parking;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.entity.car.Car;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Выезд с парковки
 */
@Data
@Slf4j
public class ParkingGateOut {

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final BlockingQueue<Car> outGate;

    public ParkingGateOut() {
        outGate = new LinkedBlockingQueue<>();
    }

    /**
     * Запустить машину в очередь к воротам на выезд
     *
     * @param car в конец очереди
     */
    public void moveIn(Car car) {
        outGate.add(car);
    }

    /**
     * Забрать машину из очереди на выезд, т.е покинуть территорию парковки
     *
     * @return Car из головы очереди
     */
    public Car moveOut() {
        Car car = outGate.remove();
        log.info("{} покинул территорию парковки", car);
        return car;
    }

    public int countCarInOutGateQueue() {
        return outGate.size();
    }
}
