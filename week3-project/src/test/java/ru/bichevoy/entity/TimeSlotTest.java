package ru.bichevoy.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.exception.IncorrectHourException;

import java.time.LocalDate;

class TimeSlotTest {

    @Test
    void overlaps1() throws IncorrectHourException {
        TimeSlot one = new TimeSlot(LocalDate.now(), 12, 14);
        TimeSlot two = new TimeSlot(LocalDate.now(), 12, 14);
        Assertions.assertTrue(one.overlaps(two));
    }

    @Test
    void overlaps2() throws IncorrectHourException {
        TimeSlot one = new TimeSlot(LocalDate.now(), 12, 14);
        TimeSlot two = new TimeSlot(LocalDate.now(), 13, 15);
        Assertions.assertTrue(one.overlaps(two));
    }

    @Test
    void overlapsNoOver() throws IncorrectHourException {
        TimeSlot one = new TimeSlot(LocalDate.now(), 11, 12);
        TimeSlot two = new TimeSlot(LocalDate.now(), 13, 14);
        Assertions.assertFalse(one.overlaps(two));
    }

    @Test
    void overlapsNoOver2() throws IncorrectHourException {
        TimeSlot one = new TimeSlot(LocalDate.now(), 12, 14);
        TimeSlot two = new TimeSlot(LocalDate.now(), 14, 18);
        Assertions.assertFalse(one.overlaps(two));
    }

    @Test
    void overlapsNoOverDifferDay() throws IncorrectHourException {
        TimeSlot one = new TimeSlot(LocalDate.now(), 12, 14);
        TimeSlot two = new TimeSlot(LocalDate.now().plusDays(1), 12, 14);
        Assertions.assertFalse(one.overlaps(two));
    }

    @Test
    void incorrectHourException1() {
        Assertions.assertThrows(IncorrectHourException.class, () -> new TimeSlot(LocalDate.now(), 14, 12));
    }

    @Test
    void incorrectHourException2() {
        Assertions.assertThrows(IncorrectHourException.class, () -> new TimeSlot(LocalDate.now(), 14, 33));
    }

    @Test
    void incorrectHourException3() {
        Assertions.assertThrows(IncorrectHourException.class, () -> new TimeSlot(LocalDate.now(), 33, 14));
    }
}