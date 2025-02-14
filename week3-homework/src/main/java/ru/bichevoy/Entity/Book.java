package ru.bichevoy.Entity;

import java.util.Objects;

public abstract class Book {

    private boolean isRented;
    private final String author;
    private final String title;


    public Book(String author, String title) {
        this.author = author;
        this.title = title;

    }

    public void rent() {
        isRented = true;
    }
    void unRent() {
        isRented = false;
    }

    public boolean isRented() {
        return isRented;
    }

    public String getAuthor() {
        return author;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Book{" +
                "isRented=" + isRented +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isRented == book.isRented && Objects.equals(author, book.author) && Objects.equals(title, book.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isRented, author, title);
    }
}
