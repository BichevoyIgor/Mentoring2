package ru.bichevoy.entity;

import java.util.*;

public class Shedule {

    private static final Shedule SHEDULE = new Shedule();
    private final Set<Lesson> lessons;

    private Shedule() {
        lessons = new HashSet<>();
    }

    public static Shedule getInstance() {
        return SHEDULE;
    }

    public void addLesson(Lesson lesson) {
        lessons.add(lesson);
    }


    @Override
    public String toString() {
        return "Shedule{" +
                "lessons=" + lessons +
                '}';
    }
}
