package ru.bichevoy.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Author;
import ru.bichevoy.exception.BaseException;
import ru.bichevoy.service.AuthorService;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class AuthorBookDAOImpl implements AuthorBookDAO {

    private final Connection connection;
    private final AuthorService authorService;

    public AuthorBookDAOImpl(DataSource dataSource, AuthorService authorService) throws SQLException {
        this.connection = dataSource.getConnection();
        this.authorService = authorService;
    }

    @Override
    public void addAuthorBook(Long authorId, Long bookId) {
        String ADD_LINE = "INSERT INTO library.authors_book (author_id, book_id) VALUES (?,?) ON CONFLICT (author_id, book_id) DO NOTHING";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_LINE);
            preparedStatement.setLong(1, authorId);
            preparedStatement.setLong(2, bookId);
            preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Author> foundAuthorByBookId(long bookId) {
        String GET_AUTHOR_ID = "SELECT author_id FROM library.authors_book WHERE book_id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_AUTHOR_ID);
            preparedStatement.setLong(1, bookId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                long authorId = resultSet.getLong("author_id");
                Optional<Author> author = authorService.findById(authorId);
                author.ifPresent(authors::add);
            }
            return authors;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }
}
