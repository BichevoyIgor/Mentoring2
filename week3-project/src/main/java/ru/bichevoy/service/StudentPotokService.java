package ru.bichevoy.service;

import ru.bichevoy.entity.StudentPotok;
import ru.bichevoy.repository.StudentPotokRepository;

import java.util.Optional;
import java.util.Set;

public class StudentPotokService {

    private final StudentPotokRepository repository;

    public StudentPotokService(StudentPotokRepository repository) {
        this.repository = repository;
    }

    public void add(StudentPotok potok) {
        repository.add(potok);
    }

    public Set<StudentPotok> getAll() {
        return repository.getAll();
    }

    public Optional<StudentPotok> getStudentPotokByTitle(String title) {
        return repository.getStudentPotokByTitle(title);
    }

    public Optional<StudentPotok> getStudentPotokById(long id) {
        return repository.getStudentPotokById(id);
    }
}
