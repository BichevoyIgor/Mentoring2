package ru.bichevoy.entity;

import ru.bichevoy.entity.exception.TimeSlotIsBusyException;

import java.util.*;

public class Teacher {

    private static long idGenerator = 1;

    private final long id;
    private final String firstName;
    private final String lastName;
    private final Set<StudentGroup> studentGroups;
    private final Set<TimeSlot> bysyTime;
    private final Set<Predmet> profile;

    public Teacher(String firstName, String lastName, Predmet profile) {
        this.id = idGenerator++;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profile = new HashSet<>();
        this.profile.add(profile);
        studentGroups = new HashSet<>();
        bysyTime = new HashSet<>();
    }

    public void occupy(TimeSlot timeSlot) throws TimeSlotIsBusyException {
        if (timeSlotIsFreeCheck(timeSlot)) {
            bysyTime.add(timeSlot);
        } else {
            throw new TimeSlotIsBusyException("Преподаватель в это время занят");
        }

    }

    public boolean timeSlotIsFreeCheck(TimeSlot timeSlot){
        for (TimeSlot slot : bysyTime) {
            if (timeSlot.overlaps(slot)){
                return false;
            }
        }
        return true;
    }

    public List<Predmet> getProfile() {
        return new ArrayList<>(profile);
    }

    public long getId() {
        return id;
    }

    public void addProfile(Predmet predmet){
        profile.add(predmet);
    }

    public void removeProfile(Predmet predmet){
        profile.remove(predmet);
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return String.format("%s %s (Профиль: %s)", firstName, lastName, profile);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id && Objects.equals(firstName, teacher.firstName) && Objects.equals(lastName, teacher.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
