package ru.bichevoy.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Customer;
import ru.bichevoy.exception.BaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

@Repository
@Slf4j
public class CustomerDAOImpl implements CustomerDAO {

    private final Connection connection;

    public CustomerDAOImpl(DataSource dataSource) throws SQLException {
        connection = dataSource.getConnection();
    }

    @Override
    public Optional<Customer> findById(long id) {
        String FIND_BY_ID = "SELECT customers.first_name, customers.last_name FROM library.customers WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            Optional<Customer> foundedCustomer = Optional.empty();
            if (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(id);
                customer.setFirstName(resultSet.getString("first_name"));
                customer.setLastName(resultSet.getString("last_name"));
                foundedCustomer = Optional.of(customer);
            }
            return foundedCustomer;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public long add(String firstName, String LastName) {
        String ADD_CUSTOMER = "INSERT INTO library.customers (first_name, last_name) VALUES (?,?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(ADD_CUSTOMER, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, firstName);
            preparedStatement.setObject(2, LastName);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return preparedStatement.getGeneratedKeys().getLong("id");
            } else {
                return 0;
            }
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }
}
