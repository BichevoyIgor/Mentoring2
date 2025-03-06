package ru.bichevoy.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.car.Truck;
import ru.bichevoy.entity.parking.TruckParkingUnit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class DiscountTest {
    private Discount discount;

    @BeforeEach
    void setUp() {
        discount = new Discount();

    }

    @Test
    void makeDiscountMin() {
        VisitHistory visitHistory1 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory1.setEnd(LocalDateTime.now().plusHours(1));

        VisitHistory visitHistory2 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));

        VisitHistory visitHistory3 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));

        List<VisitHistory> visitHistoryList = new ArrayList<>();
        visitHistoryList.add(visitHistory1);
        visitHistoryList.add(visitHistory2);
        visitHistoryList.add(visitHistory3);

        discount.setMinCountVisit(3);
        discount.setMinPercent(0.5);
        double cost = 20.0;
        double totalCost = discount.makeDiscount(visitHistoryList, cost);
        Assertions.assertEquals((cost - cost * discount.getMinPercent()), totalCost);
    }

    @Test
    void notMakeDiscount() {
        VisitHistory visitHistory1 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory1.setEnd(LocalDateTime.now().plusHours(1));

        VisitHistory visitHistory2 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));

        VisitHistory visitHistory3 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));

        List<VisitHistory> visitHistoryList = new ArrayList<>();
        visitHistoryList.add(visitHistory1);
        visitHistoryList.add(visitHistory2);
        visitHistoryList.add(visitHistory3);

        discount.setMinCountVisit(4);
        discount.setMinPercent(0.5);
        double cost = 20.0;
        double totalCost = discount.makeDiscount(visitHistoryList, cost);
        Assertions.assertEquals(cost, totalCost);
    }

    @Test
    void makeDiscountMax() {
        VisitHistory visitHistory1 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory1.setEnd(LocalDateTime.now().plusHours(1));

        VisitHistory visitHistory2 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));

        VisitHistory visitHistory3 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));

        List<VisitHistory> visitHistoryList = new ArrayList<>();
        visitHistoryList.add(visitHistory1);
        visitHistoryList.add(visitHistory2);
        visitHistoryList.add(visitHistory3);

        discount.setMaxCountVisit(3);
        discount.setMaxPercent(5);
        double cost = 20.0;
        double totalCost = discount.makeDiscount(visitHistoryList, cost);
        Assertions.assertEquals((cost - cost * discount.getMaxPercent()), totalCost);
    }

    @Test
    void makeDiscountMinMax() {
        discount.setMinCountVisit(2);
        discount.setMinPercent(0.5);
        discount.setMaxCountVisit(3);
        discount.setMaxPercent(5);
        double cost = 20.0;

        List<VisitHistory> visitHistoryList = new ArrayList<>();
        double totalCost;

        VisitHistory visitHistory1 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory1.setEnd(LocalDateTime.now().plusHours(1));
        visitHistoryList.add(visitHistory1);
        totalCost = discount.makeDiscount(visitHistoryList, cost);
        Assertions.assertEquals(cost, totalCost);

        VisitHistory visitHistory2 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));
        visitHistoryList.add(visitHistory2);
        totalCost = discount.makeDiscount(visitHistoryList, cost);
        Assertions.assertEquals((cost - cost * discount.getMinPercent()), totalCost);
        Assertions.assertNotEquals((cost - cost * discount.getMaxPercent()), totalCost);


        VisitHistory visitHistory3 = new VisitHistory(LocalDateTime.now(), new Truck("test"), new TruckParkingUnit(1));
        visitHistory2.setEnd(LocalDateTime.now().plusHours(1));
        visitHistoryList.add(visitHistory3);
        totalCost = discount.makeDiscount(visitHistoryList, cost);
        Assertions.assertNotEquals((cost - cost * discount.getMinPercent()), totalCost);
        Assertions.assertEquals((cost - cost * discount.getMaxPercent()), totalCost);
    }
}