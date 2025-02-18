package ru.bichevoy.entity.classRoom;

import java.util.ArrayList;
import java.util.List;

public class ClassRoomsList {

    private static final ClassRoomsList CLASS_ROOMS_LIST = new ClassRoomsList();
    private final List<ClassRoom> classRooms;

    private ClassRoomsList() {
        classRooms = new ArrayList<>();
        initClassRoomList();
    }

    private void initClassRoomList() {
        for (int i = 1; i <= 100; i++) {
            classRooms.add(new LectureAndSeminarClassRoom(i));
        }
        for (int i = 101; i <= 150; i++) {
            classRooms.add(new LabaClassRoom(i));
        }
    }

    public static ClassRoomsList getInstance() {
        return CLASS_ROOMS_LIST;
    }

    public ClassRoom getClassRoom(int num) throws Exception {
        if (num < 0 || num > classRooms.size()){
            throw new Exception("Указанного номера аудитории не существует");
        }
        return classRooms.get(--num);
    }
}
