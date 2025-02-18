package ru.bichevoy.entity.exception;

public class TimeSlotIsBusyException extends Exception{

    public TimeSlotIsBusyException(String message) {
        super(message);
    }
}
