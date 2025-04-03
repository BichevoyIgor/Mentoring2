package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dao.BookDAO;
import ru.bichevoy.entity.Author;
import ru.bichevoy.entity.Book;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookDAO bookDAO;
    private final AuthorBookService authorBookService;

    public Optional<Book> findById(long id) {
        return bookDAO.findById(id);
    }

    public void add(Book book) {
        long bookId = bookDAO.add(book.getTitle().trim(),
                book.getGenre().trim(),
                book.getDescription().trim());

        book.setId(bookId);
        authorBookService.addAuthorBook(book.getAuthors(), book);
    }

    public List<Book> findByTitle(String title) {
        List<Book> foundedBooks = bookDAO.findByTitle(title.trim());
        for (Book book : foundedBooks) {
            List<Author> authors = authorBookService.foundAuthorByBookId(book);
            book.setAuthors(authors);
        }
        return foundedBooks;
    }
}
