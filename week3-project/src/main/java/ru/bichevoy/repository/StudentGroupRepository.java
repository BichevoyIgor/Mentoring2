package ru.bichevoy.repository;

import ru.bichevoy.entity.StudentGroup;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StudentGroupRepository {

    void add(StudentGroup group);
    Set<StudentGroup> getAll();
    List<StudentGroup> getStudentGroupByTitle(String title);
    Optional<StudentGroup> getStudentGroupById(long id);}
