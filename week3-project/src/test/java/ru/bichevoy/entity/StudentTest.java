package ru.bichevoy.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.entity.classRoom.ClassRoomsList;
import ru.bichevoy.entity.subject.Seminar;

import java.time.LocalDate;
import java.util.Set;

class StudentTest {
    private Student student;

    @BeforeEach
    void setUp() throws Exception {
        StudentPotok potok = new StudentPotok("TestPotok");
        StudentGroup group = new StudentGroup("TT-00-TT", potok);
        student = new Student("FirstName", "LastName", group);
        Lesson lesson = new Lesson(
                new Seminar(Predmet.MATH),
                new Teacher("FirstNameTest", "LastNameTest", Predmet.MATH),
                ClassRoomsList.getInstance().getClassRoom(20),
                new TimeSlot(LocalDate.now(), 10, 12),
                student.getGroup()
        );
        student.setLessons(lesson);
    }

    @Test
    void setLessons() throws Exception {
        Assertions.assertTrue(student.getAllLessons().size() == 1);

        Lesson addedLesson1 = new Lesson(
                new Seminar(Predmet.FIZIKA),
                new Teacher("FirstNameTest", "LastNameTest", Predmet.FIZIKA),
                ClassRoomsList.getInstance().getClassRoom(22),
                new TimeSlot(LocalDate.now().plusDays(1), 12, 14),
                student.getGroup()
        );
        student.setLessons(addedLesson1);
        Assertions.assertTrue(student.getAllLessons().size() == 2);

        Lesson addedLesson3 = new Lesson(
                new Seminar(Predmet.INFORMATICA),
                new Teacher("FirstNameTest", "LastNameTest", Predmet.INFORMATICA),
                ClassRoomsList.getInstance().getClassRoom(22),
                new TimeSlot(LocalDate.now(), 12, 13),
                student.getGroup()
        );
        student.setLessons(addedLesson3);
        Assertions.assertTrue(student.getAllLessons().size() == 2);

        Set<Student.LessonDetail> lessonDetailsByDay = student.getAllLessons().get(LocalDate.now());
        Assertions.assertTrue(lessonDetailsByDay.size() == 2);
    }
}