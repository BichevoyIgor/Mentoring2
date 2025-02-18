package ru.bichevoy.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.exception.IncorrectHourException;
import ru.bichevoy.entity.exception.TimeSlotIsBusyException;

import java.time.LocalDate;
import java.util.List;

class TeacherTest {
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher("FirstNameTest", "LastNameTest", Predmet.FIZIKA);
    }

    @Test
    void occupyExceptionCheckTest() {
        Assertions.assertDoesNotThrow(() -> teacher.occupy(new TimeSlot(LocalDate.now(), 9, 11)));
        Assertions.assertThrows(TimeSlotIsBusyException.class, () -> teacher.occupy(new TimeSlot(LocalDate.now(), 9, 11)));
    }

    @Test
    void timeSlotIsFreeCheckTest() throws IncorrectHourException {
        Assertions.assertTrue(teacher.timeSlotIsFreeCheck(new TimeSlot(LocalDate.now(), 12, 13)));
    }

    @Test
    void timeSlotIsFreeCheckFalseTest() throws IncorrectHourException, TimeSlotIsBusyException {
        teacher.occupy(new TimeSlot(LocalDate.now(), 12, 13));
        Assertions.assertFalse(teacher.timeSlotIsFreeCheck(new TimeSlot(LocalDate.now(), 12, 13)));
    }

    @Test
    void getProfileTest() {
        teacher.addProfile(Predmet.HISTORY);
        List<Predmet> teacherProfileList = teacher.getProfile();
        List<Predmet> list = List.of(Predmet.FIZIKA, Predmet.HISTORY);
        Assertions.assertTrue(teacherProfileList.containsAll(list));
    }

    @Test
    void addProfileTest() {
        int beforeAddingNewProfileSize = teacher.getProfile().size();
        teacher.addProfile(Predmet.HISTORY);
        int newSize = teacher.getProfile().size();
        Assertions.assertEquals(++beforeAddingNewProfileSize, newSize);
    }

    @Test
    void removeProfileTest() {
        List<Predmet> profile = teacher.getProfile();
        teacher.removeProfile(Predmet.FIZIKA);
        Assertions.assertNotEquals(profile, teacher.getProfile());
    }
}