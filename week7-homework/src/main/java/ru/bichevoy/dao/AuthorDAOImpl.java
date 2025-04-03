package ru.bichevoy.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Author;
import ru.bichevoy.exception.BaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class AuthorDAOImpl implements AuthorDAO {
    private final Connection connection;

    @Autowired
    public AuthorDAOImpl(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }

    @Override
    public Optional<Author> findById(long id) {
        String FIND_AUTHOR = "SELECT id, name FROM library.authors WHERE id=?;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_AUTHOR);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Author> foundedAuthor = Optional.empty();
            if (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setName(resultSet.getObject("name", String.class));
                foundedAuthor = Optional.of(author);
            }
            return foundedAuthor;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public long add(String authorName) {
        String ADD_AUTHOR = "INSERT INTO library.authors (name) values (?) ON CONFLICT (name) DO NOTHING;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_AUTHOR, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, authorName);
            preparedStatement.execute();
            if (preparedStatement.getGeneratedKeys().next()) {
                return preparedStatement.getGeneratedKeys().getLong("id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public List<Long> getBooksIdByAuthorId(long authorID) {
        String GET_BOOKS = "SELECT book_id FROM library.authors_book where author_id =?;";
        List<Long> foundedBookId = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_BOOKS);
            preparedStatement.setLong(1, authorID);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                foundedBookId.add(resultSet.getLong(1));
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
        return foundedBookId;
    }

    @Override
    public Optional<Author> getAuthor(String authorName) {
        String FIND_AUTHOR = "SELECT id, name FROM library.authors WHERE LOWER(name) = LOWER(?);";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_AUTHOR);
            preparedStatement.setObject(1, authorName);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Author> foundedAuthor = Optional.empty();
            if (resultSet.next()) {
                Author author = new Author();
                author.setId(resultSet.getLong("id"));
                author.setName(resultSet.getObject("name", String.class));
                return Optional.of(author);
            }
            return foundedAuthor;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }
}
