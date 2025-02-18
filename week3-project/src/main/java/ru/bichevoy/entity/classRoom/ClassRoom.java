package ru.bichevoy.entity.classRoom;

import ru.bichevoy.entity.TimeSlot;
import ru.bichevoy.entity.exception.TimeSlotIsBusyException;


import java.util.ArrayList;
import java.util.List;

public abstract class ClassRoom {
    private final int roomNumber;
    private final List<TimeSlot> bysyTime;

    public ClassRoom(int roomNumber) {
        this.roomNumber = roomNumber;
        this.bysyTime = new ArrayList<>();
    }


    public boolean occupy(TimeSlot timeSlot) throws TimeSlotIsBusyException {
        if (timeSlotIsFreeCheck(timeSlot)) {
            bysyTime.add(timeSlot);
            return true;
        } else {
            throw new TimeSlotIsBusyException("Аудитория в выбранное время занята");
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

    public List<TimeSlot> getBusyTime() {
        return List.copyOf(bysyTime);
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    @Override
    public String toString() {
        return "ClassRoom{" +
                "roomNumber=" + roomNumber +
                ", bysyTime=" + bysyTime +
                '}';
    }
}
