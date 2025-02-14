package ru.bichevoy.Entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class RentInfoTest {

    @Test
    void getFine() {
        int countDays = 3;
        RentInfo rentInfo = new RentInfo(
                new Novel("Автор", "Название"),
                new Renter("Имя", "Фамилия"),
                LocalDate.now().minusDays(countDays));

        Assertions.assertEquals(countDays * rentInfo.PENYA, rentInfo.getFine());
    }
}