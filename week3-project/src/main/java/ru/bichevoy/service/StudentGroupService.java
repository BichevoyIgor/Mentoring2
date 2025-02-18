package ru.bichevoy.service;

import ru.bichevoy.entity.Student;
import ru.bichevoy.entity.StudentGroup;
import ru.bichevoy.repository.StudentGroupRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class StudentGroupService {
    private final StudentGroupRepository repository;

    public StudentGroupService(StudentGroupRepository repository) {
        this.repository = repository;
    }

    public void add(StudentGroup group) {
        repository.add(group);
    }

    public Set<StudentGroup> getAll() {
        return repository.getAll();
    }

    public List<StudentGroup> getStudentGroupByTitle(String title) {
        return repository.getStudentGroupByTitle(title);
    }

    public Optional<StudentGroup> getStudentGroupById(long id) {
        return repository.getStudentGroupById(id);
    }

    public List<Student> getStudentListFromGroup(StudentGroup group){
        List<Student> foundedStudent = new ArrayList<>();
        Optional<StudentGroup> g = repository.getAll().stream()
                .filter(studentGroup -> studentGroup.equals(group))
                .findFirst();

        if (g.isPresent()){
            foundedStudent.addAll(g.get().getStudentsList());
        }
        return foundedStudent;
    }
}
