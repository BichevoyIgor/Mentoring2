package ru.bichevoy.repository;

import ru.bichevoy.entity.StudentGroup;
import ru.bichevoy.entity.StudentPotok;
import ru.bichevoy.service.StudentPotokService;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class InMemoryStudentGroupRepositoryImpl implements StudentGroupRepository{

    private static final Set<StudentGroup> groups = new HashSet<>();

    public InMemoryStudentGroupRepositoryImpl() {
        initTestData();
    }

    private void initTestData() {
        StudentPotokService studentPotokService = new StudentPotokService(new InMemoryStudentPotokRepositoryImpl());
        StudentPotok it = studentPotokService.getStudentPotokByTitle("Информационные технологии").get();
        groups.add(new StudentGroup("03-KT-31", it));
        groups.add(new StudentGroup("03-KT-32", it));
        groups.add(new StudentGroup("03-KT-33", it));

        StudentPotok gaz = studentPotokService.getStudentPotokByTitle("ГазМяс").get();
        groups.add(new StudentGroup("03-GAZ-31", gaz));
        groups.add(new StudentGroup("03-GAZ-32", gaz));
        groups.add(new StudentGroup("03-GAZ-33", gaz));
        groups.add(new StudentGroup("03-GAZ-34", gaz));
        groups.add(new StudentGroup("03-GAZ-35", gaz));
    }

    @Override
    public void add(StudentGroup group) {
        groups.add(group);
    }

    @Override
    public Set<StudentGroup> getAll() {
        return Set.copyOf(groups);
    }

    @Override
    public List<StudentGroup> getStudentGroupByTitle(String title) {
        return groups.stream()
                .filter(studentGroup -> studentGroup.getGroupName().equals(title))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<StudentGroup> getStudentGroupById(long id) {
        return groups.stream()
                .filter(studentGroup -> studentGroup.getId() == id)
                .findFirst();
    }
}
