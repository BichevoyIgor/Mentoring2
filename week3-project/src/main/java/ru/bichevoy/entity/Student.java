package ru.bichevoy.entity;

import ru.bichevoy.entity.classRoom.ClassRoom;
import ru.bichevoy.entity.subject.Subject;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;


public class Student implements Comparable<Student>{

    private static long idGenerator = 1;

    private final long id;
    private final String firstName;
    private final String lastName;
    private StudentGroup group;
    private final Map<LocalDate, Set<LessonDetail>> personalShedule;

    public Student(String firstName, String lastName, StudentGroup group) {
        this.id = idGenerator++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.group = group;
        personalShedule = new TreeMap<>(Comparator.naturalOrder());
        group.addStudent(this);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public StudentGroup getGroup() {
        return group;
    }

    public long getId() {
        return id;
    }

    public void setGroup(StudentGroup group) {
        this.group = group;
    }

    public void setLessons(Lesson lesson) {
        LocalDate day = lesson.getTimeSlot().getDay();
        if (personalShedule.get(day) == null) {
            Set<LessonDetail> lessons = new HashSet<>();
            lessons.add(new LessonDetail(
                    lesson.getSubject(),
                    lesson.getTeacher(),
                    lesson.getClassRoom(),
                    lesson.getTimeSlot()
            ));
            personalShedule.put(day, lessons);
        } else {
            Set<LessonDetail> lessonDetails = personalShedule.get(day);
            lessonDetails.add(new LessonDetail(
                    lesson.getSubject(),
                    lesson.getTeacher(),
                    lesson.getClassRoom(),
                    lesson.getTimeSlot()
            ));
        }
    }

    public Map<LocalDate, Set<LessonDetail>> getAllLessons() {
        return Map.copyOf(personalShedule);
    }

    public Set<LessonDetail> getLessonsForAWeek(LocalDate localDate) {
        LocalDate startOfWeek = localDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        LocalDate endOfWeek = localDate.with(TemporalAdjusters.nextOrSame(java.time.DayOfWeek.SUNDAY)).plusDays(1);

        return personalShedule.values().stream()
                .flatMap(Set::stream)
                .filter(entry -> entry.timeSlot.getDay().isBefore(endOfWeek) && entry.timeSlot.getDay().isAfter(startOfWeek))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && Objects.equals(firstName, student.firstName) && Objects.equals(lastName, student.lastName) && Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, group);
    }

    @Override
    public String toString() {
        return String.format("\nСтудент: \nИмя: %s %s, группа: %s", firstName, lastName, group.getGroupName());
    }

    @Override
    public int compareTo(Student o) {
        return this.lastName.compareTo(o.lastName);
    }

    public class LessonDetail {
        private Subject subject;
        private Teacher teacher;
        private ClassRoom classRoom;
        private TimeSlot timeSlot;

        private LessonDetail(Subject subject, Teacher teacher, ClassRoom classRoom, TimeSlot timeSlot) {
            this.subject = subject;
            this.teacher = teacher;
            this.classRoom = classRoom;
            this.timeSlot = timeSlot;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            LessonDetail that = (LessonDetail) o;
            return Objects.equals(subject, that.subject) && Objects.equals(teacher, that.teacher) && Objects.equals(classRoom, that.classRoom) && Objects.equals(timeSlot, that.timeSlot);
        }

        @Override
        public int hashCode() {
            return Objects.hash(subject, teacher, classRoom, timeSlot);
        }



        @Override
        public String toString() {

            return String.format("""
                    Предмет: %s,
                    Преподаватель: %s,
                    Номер кабинета: %s,
                    Дата/время: %s / %sч - %sч
                    """, subject.getTitle(), teacher, classRoom.getRoomNumber(), timeSlot.getDay(), timeSlot.getStartHour(), timeSlot.getEndHour());
        }
    }
}
