package ru.bichevoy.repository;

import ru.bichevoy.entity.StudentPotok;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class InMemoryStudentPotokRepositoryImpl implements StudentPotokRepository{

    public static final Set<StudentPotok> potoks = new HashSet<>();

    public InMemoryStudentPotokRepositoryImpl() {
        initTestData();
    }

    private void initTestData() {
        potoks.add(new StudentPotok("Информационные технологии"));
        potoks.add(new StudentPotok("Юридический"));
        potoks.add(new StudentPotok("Экономический"));
        potoks.add(new StudentPotok("Филологический"));
        potoks.add(new StudentPotok("ФизМат"));
        potoks.add(new StudentPotok("ГазМяс"));
    }


    @Override
    public void add(StudentPotok potok) {
        potoks.add(potok);
    }

    @Override
    public Set<StudentPotok> getAll() {
        return Set.copyOf(potoks);
    }

    @Override
    public Optional<StudentPotok> getStudentPotokByTitle(String title) {
        return potoks.stream()
                .filter(potok -> potok.getTitle().equals(title))
                .findFirst();
    }

    @Override
    public Optional<StudentPotok> getStudentPotokById(long id) {
        return potoks.stream()
                .filter(potok -> potok.getId() == id)
                .findFirst();
    }
}
