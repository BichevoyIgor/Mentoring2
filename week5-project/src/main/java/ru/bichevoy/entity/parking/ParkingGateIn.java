package ru.bichevoy.entity.parking;

import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.ProgramProperties;
import ru.bichevoy.entity.car.Car;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Ворота на въезд парковки
 */
@Slf4j
public class ParkingGateIn {

    private final String QUEUE_IS_FULL = "Мест в очереди нет";
    private final String GOT_IN_LINE = "Встал в очередь ";
    private final AtomicInteger countReject = new AtomicInteger();

    private final BlockingQueue<Car> inGate;

    ParkingGateIn() {
        int countCarsInQueue = Integer.parseInt(ProgramProperties.getProperties().getProperty("gateIn.countCarsInQueue"));
        log.info("Установлено максимальное число машин в очереди на въезд: {}", countCarsInQueue);
        inGate = new LinkedBlockingQueue<>(countCarsInQueue);
    }

    /**
     * Запустить машину в очередь к воротам на въезд
     *
     * @param car в конец очереди
     */
    public void moveIn(Car car) {
        try {
            inGate.add(car);
            log.info(GOT_IN_LINE + "{}", car);
        } catch (IllegalStateException e) {
            countReject.incrementAndGet();
            log.info(QUEUE_IS_FULL);
        }
    }

    /**
     * Забрать машину из очереди на въезд, т.е пропустить на территорию парковки
     *
     * @return Car из головы очереди
     */
    public Car moveOut() {
        return inGate.poll();
    }

    public int gateInSize() {
        return inGate.size();
    }

    /**
     * Получить число отказов
     *
     * @return число отказов водителям которым не хватило места в очереди
     */
    public int getCountReject() {
        return countReject.get();
    }
}
