package ru.bichevoy.service;

import ru.bichevoy.entity.Student;
import ru.bichevoy.repository.StudentRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class StudentService {
    private final StudentRepository repository;

    public StudentService(StudentRepository studentRepository) {
        this.repository = studentRepository;
    }

    public void add(Student student) {
        repository.add(student);
    }

    public Set<Student> getAll() {
        return repository.getAll();
    }

    public List<Student> getStudentByName(String firstName, String lastName) {
        return repository.getStudentByName(firstName, lastName);
    }

    public Optional<Student> getStudentById(long id) {
        return repository.getStudentById(id);
    }

}
