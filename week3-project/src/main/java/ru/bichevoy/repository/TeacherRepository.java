package ru.bichevoy.repository;

import ru.bichevoy.entity.Teacher;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface TeacherRepository {

    void add(Teacher teacher);
    Set<Teacher> getAll();
    List<Teacher> getTeacherByName(String firstName, String lastName);
    Optional<Teacher> getTeacherById(long id);


}
