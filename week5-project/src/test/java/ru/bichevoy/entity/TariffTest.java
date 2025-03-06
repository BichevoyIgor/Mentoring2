package ru.bichevoy.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.ProgramProperties;

import java.time.LocalDateTime;
import java.util.Properties;

class TariffTest {
    private Tariff tariff;
    private Properties properties;

    @BeforeEach
    void setUp() {
        tariff = new Tariff();
        properties = ProgramProperties.getProperties();
    }

    @Test
    void loadTariffEquals() {
        double loadRate = Double.parseDouble(properties.getProperty("tariff.rate"));
        Assertions.assertEquals(tariff.getRate(), loadRate);
        int loadFreeSeconds = Integer.parseInt(properties.getProperty("tariff.freeSeconds"));
        Assertions.assertEquals(tariff.getFreeSeconds(), loadFreeSeconds);
        int loadFreeMinutes = Integer.parseInt(properties.getProperty("tariff.freeMinutes"));
        Assertions.assertEquals(tariff.getFreeMinutes(), loadFreeMinutes);
        int loadFreeHours = Integer.parseInt(properties.getProperty("tariff.freeHours"));
        Assertions.assertEquals(tariff.getFreeHours(), loadFreeHours);
    }

    @Test
    void loadTariffNotEquals() {
        double loadRate = Double.MAX_VALUE;
        Assertions.assertNotEquals(tariff.getRate(), loadRate);
        int loadFreeSeconds = Integer.MAX_VALUE;
        Assertions.assertNotEquals(tariff.getFreeSeconds(), loadFreeSeconds);
        int loadFreeMinutes = Integer.MAX_VALUE;
        Assertions.assertNotEquals(tariff.getFreeMinutes(), loadFreeMinutes);
        int loadFreeHours = Integer.MAX_VALUE;
        Assertions.assertNotEquals(tariff.getFreeHours(), loadFreeHours);
    }

    @Test
    void getCostParkingFreeSeconds() {
        tariff.setRate(22.0);
        tariff.setFreeMinutes(0);
        tariff.setFreeHours(0);
        tariff.setFreeSeconds(5);
        double costParking = tariff.getCostParking(LocalDateTime.now(), LocalDateTime.now().plusSeconds(5));
        Assertions.assertEquals(costParking, 0);

        costParking = tariff.getCostParking(LocalDateTime.now(), LocalDateTime.now().plusSeconds(6));
        Assertions.assertEquals(costParking, tariff.getRate());
    }

    @Test
    void getCostParkingFreeMinutes() {
        tariff.setRate(22.0);
        tariff.setFreeMinutes(5);
        tariff.setFreeHours(0);
        tariff.setFreeSeconds(0);
        double costParking = tariff.getCostParking(LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        Assertions.assertEquals(costParking, 0);

        costParking = tariff.getCostParking(LocalDateTime.now(), LocalDateTime.now().plusMinutes(6));
        Assertions.assertEquals(costParking, tariff.getRate());
    }

    @Test
    void getCostParkingFreeHours() {
        tariff.setRate(22.0);
        tariff.setFreeMinutes(0);
        tariff.setFreeHours(1);
        tariff.setFreeSeconds(0);
        double costParking = tariff.getCostParking(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
        Assertions.assertEquals(costParking, 0);

        costParking = tariff.getCostParking(LocalDateTime.now(), LocalDateTime.now().plusHours(3));
        Assertions.assertEquals(costParking, tariff.getRate() * 2);

        costParking = tariff.getCostParking(LocalDateTime.now(), LocalDateTime.now().plusHours(3).plusMinutes(5));
        Assertions.assertEquals(costParking, tariff.getRate() * 3);
    }
}