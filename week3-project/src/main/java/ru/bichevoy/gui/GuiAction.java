package ru.bichevoy.gui;

import ru.bichevoy.entity.*;
import ru.bichevoy.entity.classRoom.ClassRoom;
import ru.bichevoy.entity.classRoom.ClassRoomsList;
import ru.bichevoy.entity.exception.*;
import ru.bichevoy.entity.subject.Laba;
import ru.bichevoy.entity.subject.Lecture;
import ru.bichevoy.entity.subject.Seminar;
import ru.bichevoy.entity.subject.Subject;
import ru.bichevoy.repository.InMemoryStudentGroupRepositoryImpl;
import ru.bichevoy.repository.InMemoryStudentPotokRepositoryImpl;
import ru.bichevoy.repository.InMemoryStudentRepositoryImpl;
import ru.bichevoy.repository.InMemoryTeacherRepositoryImpl;
import ru.bichevoy.service.StudentGroupService;
import ru.bichevoy.service.StudentPotokService;
import ru.bichevoy.service.StudentService;
import ru.bichevoy.service.TeacherService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

public class GuiAction {

    private StudentPotokService studentPotokService = new StudentPotokService(new InMemoryStudentPotokRepositoryImpl());
    private TeacherService teacherService = new TeacherService(new InMemoryTeacherRepositoryImpl());
    private StudentGroupService studentGroupsService = new StudentGroupService(new InMemoryStudentGroupRepositoryImpl());
    private StudentService studentService = new StudentService(new InMemoryStudentRepositoryImpl());

    public static List<Lesson> lessons = new ArrayList<>();

    private final static Scanner scanner = new Scanner(System.in);

    public void showCommands() {
        System.out.println("\n*************************\nКоманды: \n*************************");
        for (Command command : Command.values()) {
            System.out.printf("%d) %s\n", command.orderNumber, command.translate);
        }
        System.out.println("*************************");
        System.out.println("\nВведите команду:");
    }

    public void createNewPotok() {
        System.out.println("Укажите название потока: ");
        String answerNewPotokTitle = scanner.nextLine().trim();
        Optional<StudentPotok> foundedPotokInDB = studentPotokService.getStudentPotokByTitle(answerNewPotokTitle);
        if (foundedPotokInDB.isPresent()) {
            System.out.println("Поток таким названием уже заведен в базу данных");
            return;
        }
        studentPotokService.add(new StudentPotok(answerNewPotokTitle));
        System.out.printf("Поток создан: %s\n\n", answerNewPotokTitle);
    }

    public void createNewGroup() {
        System.out.printf("Укажите название потока к которому будет относиться группа [%s]\n", getStringTitleListPotoks());
        String potokName = scanner.nextLine().trim();

        Optional<StudentPotok> potok = studentPotokService.getStudentPotokByTitle(potokName);

        if (potok.isEmpty()) {
            System.out.println("Указанного потока не существует, просьба сначала создать поток\n");
            return;
        } else {
            System.out.println("Укажите название группы: ");
            String groupName = scanner.nextLine().trim();
            long countFoundedGroup = studentGroupsService.getStudentGroupByTitle(groupName).stream().count();
            if (countFoundedGroup > 0) {
                System.out.println("Указанная группа существует");
                return;
            }
            StudentGroup newGroup = new StudentGroup(groupName, potok.get());
            System.out.printf("Группа создана: %s (%s)\n", newGroup.getGroupName(), newGroup.getStudentPotok().getTitle());
        }
    }

    public void showListPotoks() {
        System.out.println("Потоки:");
        for (StudentPotok studentPotok : studentPotokService.getAll()) {
            System.out.printf("      |- %s\n", studentPotok.getTitle());
        }
    }

    public void showStudentGroupByPotok() {

        System.out.printf("Укажите название потока: (%s)\n", getStringTitleListPotoks());
        String answer = scanner.nextLine().trim();
        for (StudentPotok studentPotok : studentPotokService.getAll()) {
            if (studentPotok.getTitle().equals(answer)) {
                for (StudentGroup studentGroup : studentPotok.getStudentGroupSet()) {
                    System.out.printf("      |- %s\n", studentGroup.getGroupName());
                }
                return;
            }
        }
        System.out.println("Указанного потока не существует");
    }

    public void createNewStudent() {
        System.out.println("Укажите имя студента");
        String firstName = scanner.nextLine().trim();
        System.out.println("Укажите фамилию студента");
        String lastName = scanner.nextLine().trim();
        try {
            StudentGroup studentGroup = getStudentGroupByDialog();
            studentService.add(new Student(firstName, lastName, studentGroup));
        } catch (IncorrectAnswer e) {
            System.out.println(e.getMessage());
            return;
        }
        System.out.println("Студент сохранен");
    }

    public void showStudentsList() {
        try {
            StudentGroup studentGroup = getStudentGroupByDialog();
            for (Student student : studentGroup.getStudentsList()) {
                System.out.printf("\nИмя: %s\nФамилия: %s\nГруппа: %s\n",
                        student.getFirstName(), student.getLastName(), student.getGroup().getGroupName());
            }
        } catch (IncorrectAnswer e) {
            System.out.println(e.getMessage());
        }
    }

    public void createNewTeacher() {
        System.out.println("Укажите имя преподавателя: ");
        String teacherName = scanner.nextLine().trim();
        System.out.println("Укажите фамилию преподавателя: ");
        String teacherSurName = scanner.nextLine().trim();

        String predmetsTitle = Arrays.stream(Predmet.values())
                .map(p -> p.getTitle())
                .collect(Collectors.joining(", "));

        System.out.printf("Укажите основной предмет который ведет преподаватель (%s):\n", predmetsTitle);
        String teacherPredmetAnswer = scanner.nextLine().trim();
        Optional<Predmet> predmet = Arrays.stream(Predmet.values())
                .filter(p -> p.getTitle().equals(teacherPredmetAnswer))
                .findFirst();
        if (predmet.isEmpty()) {
            System.out.println("Указан предмет которого в институте нет");
            return;
        } else {
            Teacher teacher = new Teacher(teacherName, teacherSurName, predmet.get());
            teacherService.add(teacher);
        }
    }

    public void showTeachersList() {
        System.out.println("Список учителей университета: ");
        for (Teacher teacher : teacherService.getAll()) {
            System.out.println(teacher);
        }
    }

    public void createNewLesson() {
        String predmetsTitle = Arrays.stream(Predmet.values())
                .map(p -> p.getTitle())
                .collect(Collectors.joining(", "));
        System.out.printf("Лекция для какого предмета планируется(%s):\n", predmetsTitle);

        String answerSubject = scanner.nextLine().trim();
        Predmet predmet = null;

        for (Predmet p : Predmet.values()) {
            if (p.getTitle().equals(answerSubject)) {
                predmet = p;
                break;
            }
        }
        if (predmet == null) {
            System.out.println("Указанный предмет в университете не преподается.");
            return;
        }
        final Predmet selectedPredmet = predmet;

        System.out.println("Просьба указать тип занятия (лекция, семинар, лабораторная):");
        String typeSubjectAnswer = scanner.nextLine().trim().toLowerCase();
        Subject subject = null;
        if (!List.of("лекция", "семинар", "лабораторная").contains(typeSubjectAnswer)) {
            System.out.println("Указан не верный  тип занятия");
            return;
        }

        if (typeSubjectAnswer.equals("лекция")) {
            subject = new Lecture(predmet);
        } else if (typeSubjectAnswer.equals("семинар")) {
            subject = new Seminar(predmet);
        } else {
            subject = new Laba(predmet);
        }


        Teacher teacher = null;
        try {
            teacher = getTeacherByDialog(selectedPredmet);
        } catch (IncorrectAnswer e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Укажите номер аудитории");
        ClassRoom classRoom = null;
        try {
            classRoom = ClassRoomsList.getInstance().getClassRoom(Integer.parseInt(scanner.nextLine().trim()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        TimeSlot timeSlot = null;
        try {
            timeSlot = getTimeSlotByDialog();
        } catch (IncorrectAnswer | IncorrectHourException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Укажите номер группы или групп через запятую (группа1, группа2)");
        String[] groupAnswer = scanner.nextLine().trim().split(", ");
        StudentGroup[] studentGroupsForLesson = new StudentGroup[groupAnswer.length];
        for (int i = 0; i < groupAnswer.length; i++) {
            final int index = i;
            Optional<StudentGroup> o = studentGroupsService.getAll().stream()
                    .filter(g -> g.getGroupName().equals(groupAnswer[index]))
                    .findFirst();
            if (o.isPresent()) {
                studentGroupsForLesson[i] = o.get();
            }
        }

        Lesson lesson = null;
        try {
            lesson = new Lesson(subject, teacher, classRoom, timeSlot, studentGroupsForLesson);
        } catch (NotAppropriateClassRoomTypeException | IncorrectProfileTeacherException | TimeSlotIsBusyException e) {
            System.out.println(e.getMessage());
            return;
        }
        lessons.add(lesson);
        Shedule.getInstance().addLesson(lesson);
        System.out.println("Урок запланирован");
        System.out.println(lesson);
    }

    public void showSheduleForStudent() {
        System.out.println("Укажите имя студента");
        String firstName = scanner.nextLine().trim();

        System.out.println("Укажите фамилию студента");
        String lastName = scanner.nextLine().trim();

        List<Student> studentByName = studentService.getStudentByName(firstName, lastName);
        if (studentByName.size() == 0) {
            System.out.println("Студент не найден");
            return;
        } else {
            for (Student student : studentByName) {
                System.out.println(student.getAllLessons());
            }
        }
    }

    public void showSheduleForStudentGroup() {
        System.out.println("Укажите номер группы:");
        String groupAnswer = scanner.nextLine();
        List<StudentGroup> foundedGroups = studentGroupsService.getStudentGroupByTitle(groupAnswer);
        if (foundedGroups.size() == 0) {
            System.out.println("Указанной группы не существует");
            return;
        }
        for (StudentGroup studentGroup : foundedGroups) {
            List<Student> studentsList = studentGroup.getStudentsList();
            if (studentsList.size() > 0) {
                System.out.println(studentsList.getFirst().getAllLessons());
            }
        }
    }

    private TimeSlot getTimeSlotByDialog() throws IncorrectAnswer, IncorrectHourException {
        System.out.printf("Укажите дату в формате ГГГГ-ММ-ДД (сегодня %s):\n", LocalDate.now());
        LocalDate dateAnswer = null;
        try {
            dateAnswer = LocalDate.parse(scanner.nextLine(), DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH));
        } catch (DateTimeParseException e) {
            throw new IncorrectAnswer("Указана не корректная дата");
        }
        System.out.println("Укажите время начала занятия:");
        int startHourAnswer = Integer.parseInt(scanner.nextLine().trim());
        System.out.println("Укажите время окончания занятия:");
        int endtHourAnswer = Integer.parseInt(scanner.nextLine().trim());

        return new TimeSlot(dateAnswer, startHourAnswer, endtHourAnswer);

    }

    private Teacher getTeacherByDialog(Predmet predmet) throws IncorrectAnswer {

        Set<Teacher> suitableTeachers = teacherService.getAll().stream()
                .filter(t -> t.getProfile().contains(predmet)).collect(Collectors.toSet());

        String teachersList = suitableTeachers.stream()
                .map(t -> String.format("%s %s", t.getFirstName(), t.getLastName()))
                .collect(Collectors.joining(", "));

        System.out.printf("Укажите имя фамилию преподавателя (%s)\n", teachersList);
        String prepodNameAnswer = scanner.nextLine().trim();
        for (Teacher suitableTeacher : suitableTeachers) {
            String name = suitableTeacher.getFirstName() + " " + suitableTeacher.getLastName();
            if (name.equals(prepodNameAnswer)) {
                return suitableTeacher;
            }
        }
        throw new IncorrectAnswer("Указанный преподаватель не найден");

    }

    private StudentGroup getStudentGroupByDialog() throws IncorrectAnswer {
        StudentPotok potok = getStudentPotokByDialog();

        String groupsName = potok.getStudentGroupSet().stream()
                .map(group -> group.getGroupName())
                .collect(Collectors.joining(", "));

        System.out.printf("Укажите группу студента (%s)\n", groupsName);
        String groupAswer = scanner.nextLine().trim();

        Optional<StudentGroup> group = potok.getStudentGroupSet().stream()
                .filter(studentGroup -> (studentGroup.getGroupName().equals(groupAswer)) && studentGroup.getStudentPotok().equals(potok))
                .findFirst();

        if (group.isEmpty()) {
            throw new IncorrectAnswer("Указано не верное название группы");
        } else {
            return group.get();
        }
    }

    private StudentPotok getStudentPotokByDialog() throws IncorrectAnswer {
        System.out.printf("Просьба указать поток (%s)\n", getStringTitleListPotoks());
        String potokAnswer = scanner.nextLine().trim();

        Optional<StudentPotok> potok = studentPotokService.getStudentPotokByTitle(potokAnswer);
        if (potok.isEmpty()) {
            throw new IncorrectAnswer("Указано не верное название потока");
        } else {
            return potok.get();
        }
    }

    private String getStringTitleListPotoks() {
        return studentPotokService.getAll().stream()
                .map(studentPotok -> studentPotok.getTitle())
                .collect(Collectors.joining(", "));
    }
    

}
