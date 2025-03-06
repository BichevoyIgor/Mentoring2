package ru.bichevoy.repository;

import ru.bichevoy.entity.VisitHistory;
import ru.bichevoy.entity.car.Car;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryVisitHistoryRepository implements VisitHistoryRepository {

    private final Map<Car, List<VisitHistory>> repository = new HashMap<>();

    @Override
    public void add(VisitHistory visitHistory) {
        Car car = visitHistory.getCar();
        List<VisitHistory> visitHistories = repository.getOrDefault(car, new ArrayList<>());
        visitHistories.add(visitHistory);
        repository.put(car, visitHistories);
    }

    @Override
    public List<VisitHistory> get(Car car) {
        return List.copyOf(repository.get(car));
    }
}
