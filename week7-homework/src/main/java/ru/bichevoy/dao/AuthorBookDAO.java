package ru.bichevoy.dao;

import ru.bichevoy.entity.Author;

import java.util.List;

public interface AuthorBookDAO {
    void addAuthorBook(Long authorId, Long bookId);

    List<Author> foundAuthorByBookId(long id);
}
