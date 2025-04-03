package ru.bichevoy.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Book;
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
public class BookDAOImpl implements BookDAO {

    private final Connection connection;

    @Autowired
    public BookDAOImpl(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }

    @Override
    public Optional<Book> findById(long id) {
        String FOUND_BY_ID = "SELECT id, title, genre, description FROM library.books WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FOUND_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Book> foundedBook = Optional.empty();
            if (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setGenre(resultSet.getString("genre"));
                book.setDescription(resultSet.getString("description"));
                foundedBook = Optional.of(book);
            }
            return foundedBook;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public long add(String title, String genre, String description) {
        String ADD_BOOK = "INSERT INTO library.books (title, genre, description) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_BOOK, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, title);
            preparedStatement.setObject(2, genre);
            preparedStatement.setObject(3, description);
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
    public List<Book> findByTitle(String title) {
        String FIND_BY_TITLE = "SELECT id, title, genre, description FROM library.books WHERE LOWER(title)=LOWER(?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_TITLE);
            preparedStatement.setObject(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Book> bookList = new ArrayList<>();
            while (resultSet.next()) {
                Book book = new Book();
                book.setId(resultSet.getLong("id"));
                book.setTitle(resultSet.getString("title"));
                book.setGenre(resultSet.getString("genre"));
                book.setDescription(resultSet.getString("description"));
                bookList.add(book);
            }
            return bookList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }
}
