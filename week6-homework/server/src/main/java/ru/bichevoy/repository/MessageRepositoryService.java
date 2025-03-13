package ru.bichevoy.repository;

import java.util.List;

public class MessageRepositoryService {
    private final MessageRepositoryImpl repository;

    public MessageRepositoryService(MessageRepositoryImpl repository) {
        this.repository = repository;
    }

    public void add(String message){
        repository.add(message);
    }
    public List<String> getMessage(int count){
        return repository.getMessage(count);
    }
}
