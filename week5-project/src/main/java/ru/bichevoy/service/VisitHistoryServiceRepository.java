package ru.bichevoy.service;

import ru.bichevoy.entity.VisitHistory;
import ru.bichevoy.entity.car.Car;
import ru.bichevoy.repository.VisitHistoryRepository;

import java.util.List;

public class VisitHistoryServiceRepository {

    private final VisitHistoryRepository visitHistoryRepository;

    public VisitHistoryServiceRepository(VisitHistoryRepository visitHistoryRepository) {
        this.visitHistoryRepository = visitHistoryRepository;
    }

    public synchronized void add(VisitHistory visitHistory) {
        visitHistoryRepository.add(visitHistory);
    }

    public synchronized List<VisitHistory> get(Car car) {
        return visitHistoryRepository.get(car);
    }
}
