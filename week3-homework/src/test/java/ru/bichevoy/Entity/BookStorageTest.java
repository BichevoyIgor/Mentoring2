package ru.bichevoy.Entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.bichevoy.Entity.Exception.NoBookInStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookStorageTest {
    private BookStorage bookStorage;
    private final String BULGAKOV = "Булгаков";
    private final String TOLSTOY = "Толстой";

    @BeforeEach
    public void init() {
        bookStorage = new BookStorage();
    }

    @Test
    void findBookTest() throws NoBookInStorage {
        bookStorage.addBook(new Novel(TOLSTOY, "Война и мир"));
        bookStorage.addBook(new Drama(BULGAKOV, "Мастер и Маргарита"));

        Book warAndPeace = new Novel(TOLSTOY, "Война и мир");

        Assertions.assertEquals(warAndPeace, bookStorage.findBook(TOLSTOY,"Война и мир"));
    }

    @Test
    void findBookThrowExceptionTest() {
        Assertions.assertThrows(NoBookInStorage.class, () -> bookStorage.findBook(TOLSTOY,"Не существующая книга"));
    }

    @Test
    void getAllBooksByAuthorTest() {
        bookStorage.addBook(new Novel(TOLSTOY, "Война и мир"));
        bookStorage.addBook(new Novel(TOLSTOY, "Книга 1"));
        bookStorage.addBook(new Novel(TOLSTOY, "Книга 2"));
        bookStorage.addBook(new Drama(BULGAKOV, "Мастер и Маргарита"));

        List<Book> books = List.of(
                new Novel(TOLSTOY, "Война и мир"),
                new Novel(TOLSTOY, "Книга 1"),
                new Novel(TOLSTOY, "Книга 2")
                );
        List<Book> allBooksByAuthor = bookStorage.getAllBooksByAuthor(TOLSTOY);
        Assertions.assertTrue(allBooksByAuthor.size() == books.size() && allBooksByAuthor.containsAll(books));
    }

    @Test
    void returnBookFromRentIfNoFineTest() {
        int countDays = 2;
        Book book = new Novel(TOLSTOY, "Книга 1");
        Renter renter = new Renter("Test", "Test");
        bookStorage.addBook(book);
        bookStorage.takeBookARent(book, renter, LocalDate.now().plusDays(countDays));
        Assertions.assertEquals(0.0, bookStorage.returnBookFromRent(renter, book));
    }

    @Test
    void returnBookFromRentIfYesFineTest() {
        int countDays = 2;
        Book book = new Novel(TOLSTOY, "Книга 1");
        Renter renter = new Renter("Test", "Test");
        bookStorage.addBook(book);
        bookStorage.takeBookARent(book, renter, LocalDate.now().plusDays(-countDays));
        Assertions.assertEquals(RentInfo.PENYA * 2, bookStorage.returnBookFromRent(renter, book));
    }
}