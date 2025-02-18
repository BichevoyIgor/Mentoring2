package ru.bichevoy.entity;

import ru.bichevoy.entity.exception.IncorrectHourException;

import java.time.LocalDate;
import java.util.Objects;

public class TimeSlot {

    private final LocalDate day;
    private final int startHour;
    private final int endHour;

    public TimeSlot(LocalDate day, int startHour, int endHour) throws IncorrectHourException{
        checkTime(startHour, endHour);
        this.day = day;
        this.startHour = startHour;
        this.endHour = endHour;
    }

    private void checkHour(int hour) throws IncorrectHourException{

    }

    private void checkTime(int startHour, int endHour) throws IncorrectHourException {

        if (startHour < 0 || startHour > 24){
            throw new IncorrectHourException(String.format("Не верно указан час %d", startHour));
        }

        if (endHour < 0 || endHour > 24){
            throw new IncorrectHourException(String.format("Не верно указан час %d", endHour));
        }

        if (startHour > endHour){
            throw new IncorrectHourException("Время начала не может быть больше времени окончания");
        }
        if (startHour == endHour){
            throw new IncorrectHourException("Время начала равно времени окончания");
        }
    }

    public boolean overlaps(TimeSlot other) {
        return (day.isEqual(other.day) && this.startHour < other.endHour && this.endHour > other.startHour);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return startHour == timeSlot.startHour && endHour == timeSlot.endHour && Objects.equals(day, timeSlot.day);
    }

    @Override
    public int hashCode() {
        return Objects.hash(day, startHour, endHour);
    }

    public LocalDate getDay() {
        return day;
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    @Override
    public String toString() {
        return "TimeSlot{" +
                "day=" + day +
                ", startHour=" + startHour +
                ", endHour=" + endHour +
                '}';
    }
}
