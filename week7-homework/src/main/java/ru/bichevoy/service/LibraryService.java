package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.entity.Author;
import ru.bichevoy.entity.Book;
import ru.bichevoy.entity.Customer;
import ru.bichevoy.entity.RentDetail;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LibraryService {
    private final BookService bookService;
    private final CustomerService customerService;
    private final AuthorService authorService;
    private final RentDetailService rentDetailService;
    private final GutendexServise gutendexServise;

    public List<Book> findBookByTitle(String title) {
        return bookService.findByTitle(title);
    }

    public void addBook(Book book, String authors) {
        String[] authorArray = authors.split(",");
        List<Author> authorList = new ArrayList<>();
        for (String author : authorArray) {
            authorList.add(authorService.getOrAddAuthor(author.trim()));
        }
        book.setAuthors(authorList);
        bookService.add(book);
    }

    public void addCustomer(Customer customer) {
        customerService.add(customer);
    }

    public List<Book> findBookInAPI(String title) {
        return gutendexServise.findBookByTitle(title);
    }

    public Optional<Customer> findCustomer(long id) {
        return customerService.findCustomerById(id);
    }

    public Optional<RentDetail> rentBook(String title, long id) {
        Optional<Customer> foundedCustomer = findCustomer(id);
        List<Book> bookByTitle = findBookByTitle(title);
        Optional<RentDetail> rentDetail = Optional.empty();
        if (foundedCustomer.isPresent() && bookByTitle.size() == 1) {
            rentDetail = Optional.of(rentDetailService.rentBook(foundedCustomer.get(), bookByTitle.get(0), LocalDate.now()));
        }
        return rentDetail;
    }

    public List<RentDetail> findRentDetails(long id) {
        return rentDetailService.findRentDetailsByCustomerId(id);
    }

    public void endRent(long id) {
        rentDetailService.endRent(id, LocalDate.now());
    }

    public List<RentDetail> findAllRentDetails(long customerId) {
        return rentDetailService.findAllRentDetailsByCustomerId(customerId);
    }
}
