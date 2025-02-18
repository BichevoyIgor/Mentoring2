package ru.bichevoy.entity.exception;

public class NotAppropriateClassRoomTypeException extends Exception{

    public NotAppropriateClassRoomTypeException() {
        super("Предмет нельзя провести в выбранной аудитории");
    }
}
