package ru.bichevoy.gui;

public enum Command {
    //EXIT("Выход из создания записи", 0),
    CREATE_NEW_POTOK("Создать поток", 1),
    SHOW_LIST_POTOKS("Показать список потоков", 2),
    CREATE_STUDENT_GROUP("Создать группу", 3),
    SHOW_LIST_STUDENT_GROUPS("Показать список групп студентов", 4),
    CREATE_NEW_STUDENT("Создать студента", 5),
    SHOW_LIST_STUDENTS("Показать студентов группы", 6),
    CREATE_NEW_TEACHER("Создать учителя", 7),
    SHOW_LIST_TEACHERS("Показать список учителей", 8),
    CREATE_NEW_LESSON("Запланировать урок", 9),
    SHOW_SHEDULE_FOR_STUDENT("Показать расписание студента", 10),
    SHOW_SHEDULE_FOR_GROUP("Показать расписание группы", 11);




    final String translate;
    final int orderNumber;

    Command(String translate, int orderNumber) {
        this.translate = translate;
        this.orderNumber = orderNumber;
    }

    public String getTranslate() {
        return translate;
    }

    public int getOrderNumber() {
        return orderNumber;
    }
}
