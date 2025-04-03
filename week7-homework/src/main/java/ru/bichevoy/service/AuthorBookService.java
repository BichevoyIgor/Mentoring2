package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dao.AuthorBookDAO;
import ru.bichevoy.entity.Author;
import ru.bichevoy.entity.Book;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorBookService {
    private final AuthorBookDAO authorBookDAO;

    public void addAuthorBook(List<Author> authors, Book book) {
        for (Author author : authors) {
            authorBookDAO.addAuthorBook(author.getId(), book.getId());
        }
    }

    public List<Author> foundAuthorByBookId(Book book) {
        return authorBookDAO.foundAuthorByBookId(book.getId());
    }
}
