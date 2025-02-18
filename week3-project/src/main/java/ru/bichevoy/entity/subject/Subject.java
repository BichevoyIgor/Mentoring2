package ru.bichevoy.entity.subject;


import ru.bichevoy.entity.Predmet;

public abstract class Subject {
    private final Predmet predmet;

    public Subject(Predmet predmet) {
        this.predmet = predmet;
    }

    public String getTitle() {
        return predmet.toString();
    }

    public Predmet getPredmet() {
        return predmet;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "title='" + predmet + '\'' +
                '}';
    }
}
