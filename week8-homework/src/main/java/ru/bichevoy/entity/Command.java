package ru.bichevoy.entity;

import lombok.Getter;

public enum Command {
    EXIT_GAME("/exit", "выход из программы"),
    ADD_CARD("/add_card", "добавить карточку"),
    REMOVE_CARD("/remove_card", "удалить карточку"),
    SHOW_ALL_CARD("/show_all_card", "показать все карточки"),
    GET_WORD("/word", "запросить случайное слово");

    @Getter
    private final String val;
    private final String description;

    Command(String val, String description) {
        this.val = val;
        this.description = description;
    }

    @Override
    public String toString() {
        return String.format("%s - %s", val, description);
    }
}
