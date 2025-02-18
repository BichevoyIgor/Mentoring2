package ru.bichevoy.entity;

import ru.bichevoy.entity.classRoom.ClassRoom;
import ru.bichevoy.entity.classRoom.LabaClassRoom;
import ru.bichevoy.entity.exception.IncorrectProfileTeacherException;
import ru.bichevoy.entity.exception.NotAppropriateClassRoomTypeException;
import ru.bichevoy.entity.exception.TimeSlotIsBusyException;
import ru.bichevoy.entity.subject.Laba;
import ru.bichevoy.entity.subject.Subject;

import java.util.*;

public class Lesson {

    private static long idGenerator = 1;

    private final long id;
    private Subject subject;
    private Teacher teacher;
    public final Set<StudentGroup> studentGroups;
    private ClassRoom classRoom;
    private TimeSlot timeSlot;

    public Lesson(Subject subject, Teacher teacher, ClassRoom classRoom, TimeSlot timeSlot, StudentGroup... studentGroup) throws TimeSlotIsBusyException, NotAppropriateClassRoomTypeException, IncorrectProfileTeacherException {
        this.id = idGenerator++;
        this.subject = subject;
        this.teacher = teacher;
        this.timeSlot = timeSlot;
        this.classRoom = classRoom;
        this.studentGroups = new HashSet<>();
        checkProfileTeacherForSubject(subject, teacher);
        teacher.timeSlotIsFreeCheck(timeSlot);
        teacher.occupy(timeSlot);
        addGroupInLesson(studentGroup);
        checkClassRoomForSubject(subject, classRoom);
        classRoom.occupy(timeSlot);
    }

    public void addGroupInLesson(StudentGroup... studentGroup) throws TimeSlotIsBusyException {
        if (studentGroup.length > 0) {
            for (StudentGroup group : studentGroup) {
                group.occupy(timeSlot);
                group.getStudentsList().stream().forEach(student -> student.setLessons(this));
                studentGroups.addAll(Arrays.asList(studentGroup));
            }
        }
    }

    private void checkProfileTeacherForSubject(Subject subject, Teacher teacher) throws IncorrectProfileTeacherException {
        if (!teacher.getProfile().contains(subject.getPredmet())) {
            throw new IncorrectProfileTeacherException("Преподаватель не ведет такой предмет");
        }
    }

    private void checkClassRoomForSubject(Subject subject, ClassRoom classRoom) throws NotAppropriateClassRoomTypeException {

        if (subject instanceof Laba) {
            if (!(classRoom instanceof LabaClassRoom))
                throw new NotAppropriateClassRoomTypeException();
        }
        if (!(subject instanceof Laba)) {
            if (classRoom instanceof LabaClassRoom) {
                throw new NotAppropriateClassRoomTypeException();
            }
        }
    }

    public Subject getSubject() {
        return subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id == lesson.id && Objects.equals(subject, lesson.subject) && Objects.equals(teacher, lesson.teacher) && Objects.equals(classRoom, lesson.classRoom) && Objects.equals(timeSlot, lesson.timeSlot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, teacher, classRoom, timeSlot);
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "subject=" + subject +
                ", teacher=" + teacher +
                ", studentGroup=" + studentGroups +
                ", classRoom=" + classRoom +
                '}';
    }
}
