package ru.bichevoy.entity;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import ru.bichevoy.ProgramProperties;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Properties;

@Slf4j
@Data
public class Tariff {

    private double rate;
    private int freeSeconds;
    private int freeMinutes;
    private int freeHours;


    public Tariff() {
        loadTariff();
    }

    public void loadTariff() {
        Properties properties = ProgramProperties.getProperties();
        try {
            rate = Double.parseDouble(properties.getProperty("tariff.rate"));
            freeSeconds = Integer.parseInt(properties.getProperty("tariff.freeSeconds"));
            freeMinutes = Integer.parseInt(properties.getProperty("tariff.freeMinutes"));
            freeHours = Integer.parseInt(properties.getProperty("tariff.freeHours"));
            log.info(String.format("Установлены значения тарифа[Стоимость: %.2f, кол-во бесплатных секунд: %d, кол-во бесплатных минут: %d, кол-во бесплатных часов: %d]",
                    rate, freeSeconds, freeMinutes, freeHours));
        } catch (NumberFormatException e) {
            log.info(e.getStackTrace().toString());
            throw e;
        }
    }

    public double getCostParking(LocalDateTime start, LocalDateTime end) {
        Duration duration = Duration.between(start, end);
        long seconds = duration.getSeconds();
        int freeTime = freeSeconds + freeMinutes * 60 + freeHours * 3600;
        if (seconds <= freeTime) {
            return 0.0;
        }
        if (seconds > freeTime) {
            seconds -= freeTime;
        }
        return Math.ceil(seconds / 3600.0) * rate;
    }
}
