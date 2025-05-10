package ru.bichevoy;

import ru.bichevoy.entity.*;
import ru.bichevoy.entity.classRoom.ClassRoomsList;
import ru.bichevoy.entity.exception.IncorrectHourException;
import ru.bichevoy.entity.exception.IncorrectProfileTeacherException;
import ru.bichevoy.entity.exception.NotAppropriateClassRoomTypeException;
import ru.bichevoy.entity.exception.TimeSlotIsBusyException;
import ru.bichevoy.gui.Command;
import ru.bichevoy.gui.GuiAction;

import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);


    public static void main(String[] args) throws TimeSlotIsBusyException, NotAppropriateClassRoomTypeException, IncorrectProfileTeacherException, IncorrectHourException {

        ClassRoomsList classRoomsList = ClassRoomsList.getInstance();
        Shedule shedule = Shedule.getInstance();
        GuiAction guiAction = new GuiAction();



        while (true) {
            guiAction.showCommands();
            String answer = scanner.nextLine();
            Command commandByString = findCommandByString(answer);
            if (commandByString == null){
                System.out.println("Не верная команда");
                continue;
            }

            switch (commandByString) {
                case Command.CREATE_NEW_POTOK -> guiAction.createNewPotok();
                case Command.SHOW_LIST_POTOKS -> guiAction.showListPotoks();
                case Command.CREATE_STUDENT_GROUP -> guiAction.createNewGroup();
                case Command.SHOW_LIST_STUDENT_GROUPS -> guiAction.showStudentGroupByPotok();
                case Command.CREATE_NEW_STUDENT -> guiAction.createNewStudent();
                case Command.SHOW_LIST_STUDENTS -> guiAction.showStudentsList();
                case Command.CREATE_NEW_TEACHER -> guiAction.createNewTeacher();
                case Command.SHOW_LIST_TEACHERS -> guiAction.showTeachersList();
                case Command.CREATE_NEW_LESSON -> guiAction.createNewLesson();
                case Command.SHOW_SHEDULE_FOR_STUDENT -> guiAction.showSheduleForStudent();
                case Command.SHOW_SHEDULE_FOR_GROUP -> guiAction.showSheduleForStudentGroup();
            }
        }
    }

    public static Command findCommandByString(String s) {
        for (Command command : Command.values()) {
            if (command.getTranslate().equalsIgnoreCase(s) || (String.valueOf(command.getOrderNumber())).equals(s)) {
                return command;
            }
        }
        return null;
    }

//    private static void testDataInit(ClassRoomsList classRoomsList, Shedule shedule){
//        StudentPotok potok = new StudentPotok("IT");
//        StudentGroup kt31 = new StudentGroup("03-КТ-31", potok);
//        StudentGroup kt33 = new StudentGroup("03-КТ-33", potok);
//        StudentGroup kt32 = new StudentGroup("03-КТ-32", potok);
//        GuiAction.studentGroups.add(kt31);
//        GuiAction.studentGroups.add(kt32);
//        GuiAction.studentPotoks.add(potok);
//        GuiAction.studentPotoks.add(new StudentPotok("Юридический"));
//        GuiAction.studentPotoks.add(new StudentPotok("Экономический"));
//        GuiAction.studentPotoks.add(new StudentPotok("Энергетика"));
//        Student student = new Student("Igor", "Bichevoy", kt31);
//        Student student2 = new Student("Sidor", "Sidorov", kt31);
//        Teacher mathTeacher = new Teacher("Anatoli", "Математик", Predmet.MATH);
//        Teacher russkiTeacher = new Teacher("Anton", "Руссист", Predmet.RUSSKI);
//        Teacher russkiTeacher2 = new Teacher("aaaaa", "Руссист", Predmet.RUSSKI);
//        GuiAction.teachers.add(mathTeacher);
//        GuiAction.teachers.add(russkiTeacher2);
//        GuiAction.teachers.add(russkiTeacher);
    }
//}



//    private static void testDataInit(ClassRoomsList classRoomsList, Shedule shedule) throws TimeSlotIsBusyException, NotAppropriateClassRoomTypeException, IncorrectProfileTeacherException, IncorrectHourException{
//        StudentPotok potok = new StudentPotok("IT");
//        StudentGroup kt31 = new StudentGroup("03-КТ-31", potok);
//        StudentGroup kt33 = new StudentGroup("03-КТ-33", potok);
//        StudentGroup kt32 = new StudentGroup("03-КТ-32", potok);
//        studentsInit(kt31);
//        studentsInit(kt32);
//        studentsInit(kt33);
//
//        Teacher mathTeacher = new Teacher("Anatoli", "Математик", Predmet.MATH);
//        Teacher russkiTeacher = new Teacher("Anton", "Руссист", Predmet.RUSSKI);
//        Teacher informaticaTeacher = new Teacher("Annan", "Информатик", Predmet.INFORMATICA);
//        Teacher historyTeacher = new Teacher("Andrey", "Историк", Predmet.HISTORY);
//        Teacher fizikaTeacher = new Teacher("Ilya", "Физист", Predmet.FIZIKA);
//        Teacher elctrotechTeacher = new Teacher("Bill", "Электротехнист", Predmet.ELECTROTECHNIKA);
//
//        Subject subject = new Seminar(Predmet.MATH);
//        ClassRoom classRoom = classRoomsList.getClassRoom(100);
//        TimeSlot timeSlot = new TimeSlot(LocalDate.now().plusDays(5), 9, 12);
//        Lesson lesson = new Lesson(subject, mathTeacher, classRoom, timeSlot, kt31);
//        shedule.addLesson(lesson);
//
//        Subject subject2 = new Seminar(Predmet.RUSSKI);
//        ClassRoom classRoom2 = classRoomsList.getClassRoom(50);
//        TimeSlot timeSlot2 = new TimeSlot(LocalDate.now(), 12, 14);
//        Lesson lesson2 = new Lesson(subject2, russkiTeacher, classRoom2, timeSlot2, kt31, kt32);
//        lesson2.addGroupInLesson(kt33);
//        shedule.addLesson(lesson2);
//
//        Subject subject3 = new Seminar(Predmet.INFORMATICA);
//        ClassRoom classRoom3 = classRoomsList.getClassRoom(55);
//        TimeSlot timeSlot3 = new TimeSlot(LocalDate.now(), 14, 16);
//        Lesson lesson3 = new Lesson(subject3, informaticaTeacher, classRoom3, timeSlot3, kt31);
//        shedule.addLesson(lesson3);
//
//        Subject subject4 = new Seminar(Predmet.HISTORY);
//        ClassRoom classRoom4 = classRoomsList.getClassRoom(55);
//        TimeSlot timeSlot4 = new TimeSlot(LocalDate.now(), 16, 18);
//        Lesson lesson4 = new Lesson(subject4, historyTeacher, classRoom4, timeSlot4, kt31);
//        shedule.addLesson(lesson4);
//
//        shedule.addLesson(
//                new Lesson(
//                        new Seminar(Predmet.MATH),
//                        mathTeacher,
//                        classRoomsList.getClassRoom(100),
//                        new TimeSlot(LocalDate.now().plusDays(1), 9, 12),
//                        kt31
//                )
//        );
//
//        shedule.addLesson(
//                new Lesson(
//                        new Seminar(Predmet.RUSSKI),
//                        russkiTeacher,
//                        classRoomsList.getClassRoom(50),
//                        new TimeSlot(LocalDate.now().plusDays(1), 12, 14),
//                        kt31
//                )
//        );
//
//        shedule.addLesson(
//                new Lesson(
//                        new Seminar(Predmet.INFORMATICA),
//                        informaticaTeacher,
//                        classRoomsList.getClassRoom(56),
//                        new TimeSlot(LocalDate.now().plusDays(1), 14, 16),
//                        kt31
//                )
//        );
//
//        shedule.addLesson(
//                new Lesson(
//                        new Seminar(Predmet.HISTORY),
//                        historyTeacher,
//                        classRoomsList.getClassRoom(56),
//                        new TimeSlot(LocalDate.now().plusDays(1), 16, 18),
//                        kt31
//                )
//        );
//    }
//
//    private static void studentsInit(StudentGroup studentGroup) {
//
//        for (int i = 0; i < 15; i++) {
//            studentGroup.addStudent(new Student("Имя-" + i,
//                    "Фамилия-" + i,
//                    studentGroup));
//        }
//    }
//}
