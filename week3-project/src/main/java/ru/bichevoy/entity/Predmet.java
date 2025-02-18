package ru.bichevoy.entity;

public enum Predmet {
    HISTORY("История"),
    INFORMATICA("Информатика"),
    RUSSKI("Русский язык"),
    MATH("Математика"),
    FIZIKA("Физика"),
    ELECTROTECHNIKA("Электротехника");

    Predmet(String title) {
        this.title = title;
    }

    final String title;

    @Override
    public String toString() {
        return  title;
    }

    public String getTitle() {
        return title;
    }
}
