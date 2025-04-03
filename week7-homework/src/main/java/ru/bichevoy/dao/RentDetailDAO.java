package ru.bichevoy.dao;

import ru.bichevoy.entity.RentDetail;

import java.sql.Date;
import java.util.List;

public interface RentDetailDAO {
    long rentBook(long customerId, long bookId, Date startDate);

    List<RentDetail> findRentDetailsByCustomerId(long id);

    List<RentDetail> findAllRentDetailsByCustomerId(long id);

    void endRent(long id, Date date);
}
