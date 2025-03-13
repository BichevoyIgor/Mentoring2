package ru.bichevoy.repository;

import java.util.List;

public interface MessageRepository {
    void add(String message);
    List<String> getMessage(int count);
}
