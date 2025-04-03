package ru.bichevoy.dao;

import ru.bichevoy.entity.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorDAO {

    Optional<Author> findById(long id);

    long add(String authorName);

    List<Long> getBooksIdByAuthorId(long authorID);

    Optional<Author> getAuthor(String authorName);
}
