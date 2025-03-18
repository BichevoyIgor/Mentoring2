package ru.bichevoy;

import lombok.Getter;

@Getter
public enum Commands {
    EXIT("/EXIT"),
    LOGIN("/LOGIN="),
    CHAT("/CHAT="),
    GAME("/GAME=");

    private final String val;

    Commands(String val) {
        this.val = val;
    }

}
