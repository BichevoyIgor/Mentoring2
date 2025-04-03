package ru.bichevoy.dao;

import ru.bichevoy.entity.Book;

import java.util.List;
import java.util.Optional;

public interface BookDAO {

    Optional<Book> findById(long id);

    List<Book> findByTitle(String title);

    long add(String title, String genre, String description);

}
