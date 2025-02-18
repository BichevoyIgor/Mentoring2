package ru.bichevoy.service;

import ru.bichevoy.entity.Teacher;
import ru.bichevoy.repository.TeacherRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class TeacherService {
    private final TeacherRepository repository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.repository = teacherRepository;
    }

    public void add(Teacher teacher){
        repository.add(teacher);
    }

    public Set<Teacher> getAll(){
        return repository.getAll();
    }

    public List<Teacher> getTeacherByName(String firstName, String lastName){
        return repository.getTeacherByName(firstName, lastName);
    }

    public Optional<Teacher> getTeacherById(long id){
        return repository.getTeacherById(id);
    }
}
