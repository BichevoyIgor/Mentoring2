package ru.bichevoy.Entity;

import ru.bichevoy.Entity.Exception.NoBookInStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class BookStorage {

    private final List<Book> bookList;
    private final Map<String, List<Book>> booksByAuthor;
    private final List<RentInfo> rentInfos;

    public BookStorage() {
        bookList = new ArrayList<>();
        booksByAuthor = new HashMap<>();
        rentInfos = new ArrayList<>();
    }

    public List<Book> getAllBooks() {
        return bookList;
    }

    public List<Book> getAllBooksByAuthor(String author) {
        return booksByAuthor.get(author);
    }

    public List<Book> getAllNoRentBooks() {
        return bookList.stream()
                .filter(book -> !book.isRented())
                .collect(Collectors.toList());
    }

    public List<RentInfo> getAllRentInfo() {
        return List.copyOf(rentInfos);
    }

    public void takeBookARent(Book book, Renter renter, LocalDate endRentDay) {
        RentInfo rentInfo = new RentInfo(book, renter, endRentDay);
        book.rent();
        rentInfos.add(rentInfo);
    }

    public double returnBookFromRent(Renter renter, Book book) {
        double fine = 0.0;
        List<RentInfo> rentInfoList = rentInfos.stream()
                .filter(record -> record.getRenter().equals(renter) && record.getBook().equals(book))
                .collect(Collectors.toList());

        if (rentInfoList.size() == 1) {
            fine = rentInfoList.get(0).getFine();
            rentInfos.remove(rentInfoList.get(0));
        } else if (rentInfoList.size() > 1) {
            RentInfo rentInfo = rentInfoList.stream()
                    .min((o1, o2) -> (int) (o1.getEndRentDate().until(o2.getEndRentDate(), ChronoUnit.DAYS)))
                    .get();
            fine = rentInfo.getFine();
            rentInfoList.remove(rentInfo);
        }
        book.unRent();
        return fine;
    }

    public Book findBook(String author, String title) throws NoBookInStorage {
        Book foundedBook = bookList.stream()
                .filter(book -> book.getTitle().equals(title) && book.getAuthor().equals(author) && !book.isRented())
                .findAny().orElseThrow(NoBookInStorage::new);
        return foundedBook;
    }

    public <T> List<T> getAllBooksByGanre(Class<T> type) {
        return bookList.stream()
                .filter(type::isInstance)
                .map(book -> (T) book)
                .collect(Collectors.toList());
    }


    public List<Book> getBooksEndRentTomorrow() {
        return rentInfos.stream()
                .filter(rentInfo -> (LocalDate.now().plusDays(1L)).equals(rentInfo.getEndRentDate()))
                .map(rentInfo -> rentInfo.getBook())
                .collect(Collectors.toList());
    }

    public RentInfo getRentInfoByBook(Book book) {
        return rentInfos.stream()
                .filter(element -> element.getBook().equals(book))
                .findFirst().orElseGet(() -> new RentInfo(null, null, null));
    }

    public void addBook(Book book) {
        bookList.add(book);
        String author = book.getAuthor();
        booksByAuthor.computeIfAbsent(author, k -> new ArrayList<>()).add(book);
    }

    public List<Novel> getAllNovels() {
        return bookList.stream()
                .filter(book -> book instanceof Novel)
                .map(book -> (Novel) book)
                .collect(Collectors.toList());
    }

    public List<Drama> getAllDrama() {
        return bookList.stream()
                .filter(book -> book instanceof Drama)
                .map(book -> (Drama) book)
                .collect(Collectors.toList());
    }

    public List<Detective> getAllDetective() {
        return bookList.stream()
                .filter(book -> book instanceof Detective)
                .map(book -> (Detective) book)
                .collect(Collectors.toList());
    }
}

