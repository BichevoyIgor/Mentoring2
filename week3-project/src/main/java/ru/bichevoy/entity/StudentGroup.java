package ru.bichevoy.entity;

import ru.bichevoy.entity.exception.TimeSlotIsBusyException;

import java.util.*;

public class StudentGroup {

    private static long idGenerator = 1;

    private final long id;
    private final String groupName;
    private final List<Student> studentsList;
    private final Set<TimeSlot> bysyTime;
    private StudentPotok studentPotok;

    public StudentGroup(String groupName, StudentPotok studentPotok) {
        this.id = idGenerator++;
        this.groupName = groupName;
        studentsList = new ArrayList<>();
        bysyTime = new HashSet<>();
        this.studentPotok = studentPotok;
        studentPotok.addGroup(this);
    }

    public long getId() {
        return id;
    }



    public StudentPotok getStudentPotok() {
        return studentPotok;
    }


    public void addStudent(Student student){
        studentsList.add(student);
    }

    public boolean removeStudent(Student student){
        return studentsList.remove(student);
    }

    public List<Student> getStudentsList() {
        return List.copyOf(studentsList);
    }

    public String getGroupName() {
        return groupName;
    }

    public boolean occupy(TimeSlot timeSlot) throws TimeSlotIsBusyException {
        if (timeSlotIsFreeCheck(timeSlot)) {
            bysyTime.add(timeSlot);
            return true;
        } else {
            throw new TimeSlotIsBusyException("У группы на это время уже установлено занятие");
        }
    }

    public boolean timeSlotIsFreeCheck(TimeSlot timeSlot){
        for (TimeSlot slot : bysyTime) {
            if (timeSlot.overlaps(slot)){
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentGroup that = (StudentGroup) o;
        return id == that.id && Objects.equals(groupName, that.groupName) && Objects.equals(studentPotok, that.studentPotok);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, groupName, studentPotok);
    }

    @Override
    public String toString() {

        return "StudentGroup{" +
                "groupName='" + groupName + '\'' +
                ", studentsList=" + studentsList  +
                '}';
    }
}
