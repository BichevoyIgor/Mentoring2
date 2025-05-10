package ru.bichevoy.repository;

import ru.bichevoy.entity.StudentPotok;

import java.util.Optional;
import java.util.Set;

public interface StudentPotokRepository {

    void add(StudentPotok potok);
    Set<StudentPotok> getAll();
    Optional<StudentPotok> getStudentPotokByTitle(String title);
    Optional<StudentPotok> getStudentPotokById(long id);
}
