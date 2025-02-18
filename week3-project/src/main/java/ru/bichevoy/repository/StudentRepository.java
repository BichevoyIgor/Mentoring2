package ru.bichevoy.repository;

import ru.bichevoy.entity.Student;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentRepository {

    void add(Student student);
    Set<Student> getAll();
    List<Student> getStudentByName(String firstName, String lastName);
    Optional<Student> getStudentById(long id);

}
