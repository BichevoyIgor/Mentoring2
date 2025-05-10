package ru.bichevoy.repository;

import ru.bichevoy.entity.Student;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryStudentRepositoryImpl implements StudentRepository{

    private static final Set<Student> students = new TreeSet<>();

    @Override
    public void add(Student student) {
        students.add(student);
    }

    @Override
    public Set<Student> getAll() {
        return Set.copyOf(students);
    }

    @Override
    public List<Student> getStudentByName(String firstName, String lastName) {
        return students.stream()
                .filter(student -> student.getFirstName().equals(firstName) && student.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Student> getStudentById(long id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst();
    }
}
