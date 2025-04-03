package ru.bichevoy.entity;

import lombok.Data;

import java.util.List;

@Data
public class Book {
    private long id;
    private String title;
    private String description;
    private List<Author> authors;
    private String genre;
}
