package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dao.AuthorDAO;
import ru.bichevoy.entity.Author;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorService {
    private final AuthorDAO authorDAO;

    public Optional<Author> findById(long id) {
        return authorDAO.findById(id);
    }

    public long add(Author author) {
        long authorId = authorDAO.add(author.getName());
        author.setId(authorId);
        return authorId;
    }

    public List<Long> getBooksByAuthor(Author author) {
        return authorDAO.getBooksIdByAuthorId(author.getId());
    }

    public Author getOrAddAuthor(String authorName) {
        Optional<Author> author = authorDAO.getAuthor(authorName);
        if (author.isEmpty()) {
            long id = authorDAO.add(authorName);
            Author newAuthor = new Author();
            newAuthor.setId(id);
            newAuthor.setName(authorName);
            return newAuthor;
        } else {
            return author.get();
        }
    }
}
