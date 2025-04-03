package ru.bichevoy.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dao.RentDetailDAO;
import ru.bichevoy.entity.Book;
import ru.bichevoy.entity.Customer;
import ru.bichevoy.entity.RentDetail;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RentDetailService {
    private final RentDetailDAO rentDetailDAO;
    private final CustomerService customerService;
    private final BookService bookService;

    public RentDetail rentBook(Customer customer, Book book, LocalDate startDate) {
        RentDetail rentDetail = new RentDetail();
        rentDetail.setId(rentDetailDAO.rentBook(
                customer.getId(),
                book.getId(),
                Date.valueOf(startDate))
        );
        rentDetail.setBook(book);
        rentDetail.setCustomer(customer);
        rentDetail.setStartDate(startDate);
        return rentDetail;
    }

    public List<RentDetail> findRentDetailsByCustomerId(long id) {
        List<RentDetail> foundedRentDetails = rentDetailDAO.findRentDetailsByCustomerId(id);
        fillRentDetails(foundedRentDetails);
        return foundedRentDetails;
    }

    public List<RentDetail> findAllRentDetailsByCustomerId(long customerId) {
        List<RentDetail> foundedRentDetails = rentDetailDAO.findAllRentDetailsByCustomerId(customerId);
        fillRentDetails(foundedRentDetails);
        return foundedRentDetails;
    }

    private void fillRentDetails(List<RentDetail> foundedRentDetails) {
        for (RentDetail rentDetail : foundedRentDetails) {
            long customerId = rentDetail.getCustomer().getId();
            Optional<Customer> foundedCustomer = customerService.findCustomerById(customerId);
            foundedCustomer.ifPresent(rentDetail::setCustomer);

            long bookId = rentDetail.getBook().getId();
            Optional<Book> foundedBook = bookService.findById(bookId);
            foundedBook.ifPresent(rentDetail::setBook);
        }
    }

    public void endRent(long id, LocalDate dateNow) {
        rentDetailDAO.endRent(id, Date.valueOf(dateNow));
    }
}
