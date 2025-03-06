package ru.bichevoy.entity.parking;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.ProgramProperties;
import ru.bichevoy.entity.Discount;
import ru.bichevoy.entity.Tariff;
import ru.bichevoy.entity.VisitHistory;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.entity.car.PassengerCar;
import ru.bichevoy.entity.car.Truck;
import ru.bichevoy.repository.InMemoryParkingCarRepository;
import ru.bichevoy.repository.InMemoryParkingSpaceRepository;
import ru.bichevoy.repository.InMemoryVisitHistoryRepository;
import ru.bichevoy.service.ParkingCarService;
import ru.bichevoy.service.ParkingSpaceService;
import ru.bichevoy.service.VisitHistoryServiceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


@Data
@Slf4j
public class ParkingSpace {
    private final ParkingSpaceService repository;
    private final ParkingCarService parkingCarService;
    private final ParkingGateIn gateIn;
    private final ParkingGateOut gateOut;
    private final VisitHistoryServiceRepository visitHistoryServiceRepository;
    private final Semaphore semaphore;
    private volatile boolean isOpen;
    private Tariff tariff;
    private Discount discount;

    public ParkingSpace() {
        semaphore = new Semaphore(1);
        repository = new ParkingSpaceService(new InMemoryParkingSpaceRepository());
        gateIn = new ParkingGateIn();
        gateOut = new ParkingGateOut();
        initParkingUnitsFromDataProperties(repository);
        parkingCarService = new ParkingCarService(new InMemoryParkingCarRepository());
        visitHistoryServiceRepository = new VisitHistoryServiceRepository(new InMemoryVisitHistoryRepository());
        tariff = new Tariff();
        discount = new Discount();
        setOpen(true);
        gateInListener();
    }

    /**
     * Состояние работы парковки
     *
     * @param open = true(Открыто)/false(Закрыто)
     */
    public void setOpen(boolean open) {
        isOpen = open;
        String message = open ? "Парковка открыта" : "Парковка закрыта";
        log.info(message);
    }

    /**
     * Генерация парковочных мест
     */
    public void initParkingUnitsFromDataProperties(ParkingSpaceService parkingSpaceService) {
        int countPassengerParkingUnit = Integer.parseInt(ProgramProperties.getProperties().getProperty("countPassengerParkingUnit"));
        int countTruckParkingUnit = Integer.parseInt(ProgramProperties.getProperties().getProperty("countTruckParkingUnit"));
        for (int i = 0; i < countPassengerParkingUnit; i++) {
            parkingSpaceService.add(new PassangerParkingUnit(parkingSpaceService.getIdForNewParkingUnit()));
        }
        for (int i = 0; i < countTruckParkingUnit; i++) {
            parkingSpaceService.add(new TruckParkingUnit(parkingSpaceService.getIdForNewParkingUnit()));
        }
    }

    /**
     * Если в очереди есть автомобили, забираем из очереди и пытаемся припарковать
     */
    private void gateInListener() {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        new Thread(() -> {
            while (isOpen) {
                pool.execute(() -> {
                    if (gateIn.gateInSize() > 0) {
                        try {
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            log.error(e.getStackTrace().toString());
                            throw new RuntimeException(e);
                        }
                        Car car = gateIn.moveOut();
                        semaphore.release();
                        if (car != null) {
                            if (car instanceof Truck) {
                                parking((Truck) car);
                            } else {
                                parking((PassengerCar) car);
                            }
                        }
                    }
                });
            }
            pool.shutdown();
        }).start();
    }

    /**
     * Ищем свободное парковочное место для легкового авто, если таких не осталось, паркуем на грузовое
     *
     * @param car
     */
    ParkingUnit parking(PassengerCar car) {
        Set<ParkingUnit> allParkingUnits = repository.getAllParkingUnits();
        for (ParkingUnit parkingUnit : allParkingUnits) {
            if ((parkingUnit instanceof PassangerParkingUnit) && parkingUnit.getCar().isEmpty()) {
                goParking(car, parkingUnit);
                return parkingUnit;
            }
        }
        for (ParkingUnit parkingUnit : allParkingUnits) {
            if (parkingUnit instanceof TruckParkingUnit
                    && ((TruckParkingUnit) parkingUnit).countFreeSpaceForPassengerCar() > 0) {
                goParking(car, parkingUnit);
                return parkingUnit;
            }
        }
        log.info("{} не нашел свободного места, поехал на выезд", car);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            log.error(e.getStackTrace().toString());
            throw new RuntimeException(e);
        }
        gateOut.moveIn(car);
        semaphore.release();
        return null;
    }

    /**
     * Ищем свободное парковочное место для грузовика
     *
     * @param truck
     */
    ParkingUnit parking(Truck truck) {
        Set<ParkingUnit> allParkingUnits = repository.getAllParkingUnits();
        for (ParkingUnit parkingUnit : allParkingUnits) {
            if (parkingUnit instanceof TruckParkingUnit && parkingUnit.getCar().isEmpty()) {
                goParking(truck, parkingUnit);
                return parkingUnit;
            }
        }
        log.info("{} не нашел свободного места, поехал на выезд", truck);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gateOut.moveIn(truck);
        semaphore.release();
        return null;
    }

    /**
     * Паркуем машину если место свободно
     *
     * @param car
     * @param parkingUnit
     */
    private void goParking(Car car, ParkingUnit parkingUnit) {
        parkingUnit.setCar(car);
        visitHistoryServiceRepository.add(new VisitHistory(LocalDateTime.now(), car, parkingUnit));
        parkingCarService.addCar(car, parkingUnit);
        log.info("{} припаркован на место {}", car, parkingUnit.getId());
    }

    /**
     * Завершить парковку и покинуть стоянку
     *
     * @param car - ссылка на авто
     */
    public void unParking(Car car) {
        ParkingUnit parkingUnit = parkingCarService.findParkingUnitByCar(car);
        parkingUnit.removeCar(car);
        parkingCarService.removeCar(car);
        log.info("{} освободил парковочное место {}", car, parkingUnit.getId());
        VisitHistory currentVisitHistory = finishCurrentVisit(car);
        double costParking = tariff.getCostParking(currentVisitHistory.getStart(), currentVisitHistory.getEnd());
        costParking = discount.makeDiscount(visitHistoryServiceRepository.get(car), costParking);
        log.info(String.format("%s заплатил %.2f", car, costParking));
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        gateOut.moveIn(car);
        semaphore.release();
    }

    /**
     * Проставить в историю посещения, дату и время завершения текущей сессии посещения
     *
     * @param car
     * @return
     */
    private VisitHistory finishCurrentVisit(Car car) {
        VisitHistory currentVisitHistory = visitHistoryServiceRepository.get(car).stream()
                .filter(v -> v.getEnd() == null)
                .findFirst()
                .get();
        currentVisitHistory.setEnd(LocalDateTime.now());
        return currentVisitHistory;
    }

    /**
     * @return количество авто на парковке
     */
    public int getCountCarsInParkingSpace() {
        return parkingCarService.getAllCars().size();
    }

    /**
     * @return список авто на парковке
     */
    public List<Car> getCarsInParkingSpace() {
        return parkingCarService.getAllCars();
    }
}
