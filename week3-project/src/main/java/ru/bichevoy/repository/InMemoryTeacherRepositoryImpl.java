package ru.bichevoy.repository;

import ru.bichevoy.entity.Predmet;
import ru.bichevoy.entity.Teacher;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTeacherRepositoryImpl implements TeacherRepository{

    public static final Set<Teacher> teachers = new HashSet<>();

    public InMemoryTeacherRepositoryImpl() {
        initTestData();
    }

    private void initTestData() {
        teachers.add(new Teacher("Ivan", "Ivanov", Predmet.FIZIKA));
        teachers.add(new Teacher("Roman", "Petrov", Predmet.RUSSKI));
        teachers.add(new Teacher("Stas", "Kirkorov", Predmet.INFORMATICA));
        teachers.add(new Teacher("Ilya", "Kluni", Predmet.ELECTROTECHNIKA));
        teachers.add(new Teacher("Han", "Gudini", Predmet.HISTORY));
        teachers.add(new Teacher("Andrey", "Kolobov", Predmet.MATH));
        teachers.add(new Teacher("Mihail", "Vasiliev", Predmet.INFORMATICA));
        teachers.add(new Teacher("Artem", "Proscurov", Predmet.RUSSKI));
        teachers.add(new Teacher("Zina", "Matvienko", Predmet.HISTORY));
    }

    @Override
    public void add(Teacher teacher) {
        teachers.add(teacher);
    }

    @Override
    public Set<Teacher> getAll() {
        return Set.copyOf(teachers);
    }

    @Override
    public List<Teacher> getTeacherByName(String firstName, String lastName) {
        return teachers.stream()
                .filter(t-> t.getFirstName().equals(firstName) && t.getLastName().equals(lastName))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Teacher> getTeacherById(long id) {
        return teachers.stream()
                .filter(t-> t.getId() == id)
                .findFirst();
    }
}
