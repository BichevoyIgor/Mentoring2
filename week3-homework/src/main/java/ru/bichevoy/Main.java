package ru.bichevoy;

import ru.bichevoy.Entity.*;
import ru.bichevoy.Entity.Exception.NoBookInStorage;
import ru.bichevoy.Entity.Exception.NoNovelsInLibraryException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws NoNovelsInLibraryException, NoBookInStorage {

        BookStorage storage = new BookStorage();

        storage.addBook(new Drama("Булгаков", "Мастер и Маргарита"));
        storage.addBook(new Novel("Толстой", "Война и мир"));
        storage.addBook(new Drama("Автор1", "Какая то книга"));
        storage.addBook(new Drama("Автор2", "Какая то книга"));
        storage.addBook(new Drama("Автор3", "Какая то книга"));
        storage.addBook(new Drama("Автор1", "Какая то книга"));
        storage.addBook(new Novel("Автор1", "Какая то книга"));

        Renter renter = new Renter("Igor", "Bichevoy");
        Book book = storage.findBook("Булгаков","Мастер и Маргарита");
        storage.takeBookARent(book, renter, getEndRentDate(1));

        Renter renter2 = new Renter("Ivan", "Ivanov");
        Book book1 = storage.findBook("Автор1", "Какая то книга");
        storage.takeBookARent(book1, renter2, getEndRentDate(3));

        System.out.println(storage.getAllRentInfo());
        storage.returnBookFromRent(renter2, book1);
        System.out.println(storage.getAllRentInfo());
    }

    private static LocalDate getEndRentDate(long countDaysForRent){
        return LocalDate.now().plusDays(countDaysForRent);
    }
}
