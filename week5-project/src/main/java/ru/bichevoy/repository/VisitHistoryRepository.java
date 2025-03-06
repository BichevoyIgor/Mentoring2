package ru.bichevoy.repository;

import ru.bichevoy.entity.VisitHistory;
import ru.bichevoy.entity.car.Car;

import java.util.List;

public interface VisitHistoryRepository {

    void add(VisitHistory visitHistory);

    List<VisitHistory> get(Car car);
}
