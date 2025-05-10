package ru.bichevoy.repository;

import java.util.ArrayList;
import java.util.List;

public class MessageRepositoryImpl implements MessageRepository {
    private final List<String> repository;

    public MessageRepositoryImpl() {
        this.repository = new ArrayList<>();
    }

    @Override
    public void add(String message) {
        repository.add(message);
    }

    @Override
    public List<String> getMessage(int count) {
        int indexFrom = Math.max((repository.size() - count), 0);
        return repository.subList(indexFrom, repository.size());
    }
}
