package ru.bichevoy.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class StudentPotok {

    private static long idGenerator = 1;

    private final long id;
    private String title;
    private final Set<StudentGroup> studentGroupSet;

    public StudentPotok(String title) {
        this.title = title;
        this.id = idGenerator++;
        studentGroupSet = new HashSet<>();
    }

    public void addGroup(StudentGroup group){
        studentGroupSet.add(group);
    }

    public long getId() {
        return id;
    }

    public void removeGroup(StudentGroup group){
        studentGroupSet.add(group);
    }

    public String getTitle() {
        return title;
    }

    public Set<StudentGroup> getStudentGroupSet() {

        return Set.copyOf(studentGroupSet);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StudentPotok that = (StudentPotok) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title);
    }

    @Override
    public String toString() {
        List<String> groupsName = studentGroupSet.stream()
                .map(StudentGroup::getGroupName).collect(Collectors.toList());

        return "StudentPotok{" +
                "title='" + title + '\'' +
                ", studentGroups=" + groupsName +
                '}';
    }
}
