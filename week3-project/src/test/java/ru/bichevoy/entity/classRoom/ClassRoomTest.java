package ru.bichevoy.entity.classRoom;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.TimeSlot;
import ru.bichevoy.entity.exception.IncorrectHourException;
import ru.bichevoy.entity.exception.TimeSlotIsBusyException;

import java.time.LocalDate;

class ClassRoomTest {

    @Test
    void occupyTest() throws IncorrectHourException, TimeSlotIsBusyException {
        ClassRoom classRoom = new LabaClassRoom(100);
        classRoom.occupy(new TimeSlot(LocalDate.now(), 11, 12));
        classRoom.occupy(new TimeSlot(LocalDate.now(), 12, 13));
        classRoom.occupy(new TimeSlot(LocalDate.now(), 13, 15));
        classRoom.occupy(new TimeSlot(LocalDate.now().plusDays(1), 14, 15));
        Assertions.assertEquals(4, classRoom.getBusyTime().size());

    }

    @Test
    void occupyThrowExceptionTest() throws TimeSlotIsBusyException, IncorrectHourException {
        ClassRoom classRoom = new LabaClassRoom(100);
        classRoom.occupy(new TimeSlot(LocalDate.now(), 13, 15));
        Assertions.assertThrows(TimeSlotIsBusyException.class, () -> classRoom.occupy(new TimeSlot(LocalDate.now(), 13, 15)));
    }

}