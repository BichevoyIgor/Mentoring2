package ru.bichevoy.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.bichevoy.entity.Book;
import ru.bichevoy.entity.Customer;
import ru.bichevoy.entity.RentDetail;
import ru.bichevoy.exception.BaseException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class RentDetailDAOImpl implements RentDetailDAO {
    private final Connection connection;

    public RentDetailDAOImpl(DataSource dataSource) throws SQLException {
        this.connection = dataSource.getConnection();
    }

    @Override
    public long rentBook(long customerId, long bookId, Date startDate) {
        String RENT_BOOK = "INSERT INTO library.rent_details (customer_id, book_id, start_date) VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(RENT_BOOK, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, bookId);
            preparedStatement.setDate(3, startDate);
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
    public List<RentDetail> findRentDetailsByCustomerId(long id) {
        String FIND_RENT_DETAILS = "SELECT rd.id, rd.customer_id, rd.book_id, rd.start_date, rd.end_date from library.rent_details rd WHERE customer_id = ? AND rd.end_date IS NULL";
        return getRentDetails(id, FIND_RENT_DETAILS);
    }

    @Override
    public List<RentDetail> findAllRentDetailsByCustomerId(long id) {
        String FIND_ALL_RENT_DETAILS = "SELECT rd.id, rd.customer_id, rd.book_id, rd.start_date, rd.end_date from library.rent_details rd WHERE customer_id = ?";
        return getRentDetails(id, FIND_ALL_RENT_DETAILS);
    }

    private List<RentDetail> getRentDetails(long id, String FIND_RENT_DETAILS) {
        try {
            List<RentDetail> rentDetailList = new ArrayList<>();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_RENT_DETAILS);
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                RentDetail rentDetail = new RentDetail();
                long rentDetailId = resultSet.getLong("id");
                long customerId = resultSet.getLong("customer_id");
                long bookId = resultSet.getLong("book_id");
                LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
                Date foundedEndDate = resultSet.getDate("end_date");

                Customer customer = new Customer();
                customer.setId(customerId);
                Book book = new Book();
                book.setId(bookId);

                rentDetail.setId(rentDetailId);
                rentDetail.setCustomer(customer);
                rentDetail.setBook(book);
                rentDetail.setStartDate(startDate);
                if (foundedEndDate != null) {
                    rentDetail.setEndDate(foundedEndDate.toLocalDate());
                }
                rentDetailList.add(rentDetail);
            }
            return rentDetailList;
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }

    @Override
    public void endRent(long id, Date endDate) {
        String END_RENT = "UPDATE library.rent_details SET end_date=? WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(END_RENT);
            preparedStatement.setDate(1, endDate);
            preparedStatement.setLong(2, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            log.error(e.getMessage());
            throw new BaseException(e.getMessage());
        }
    }
}
