package ru.bichevoy.Entity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class RentInfo {
    private final Book book;
    private final Renter renter;
    private final LocalDate endRentDate;

    static final double PENYA = 2.5;

    public RentInfo(Book book, Renter renter, LocalDate endRentDate) {
        this.book = book;
        this.renter = renter;
        this.endRentDate = endRentDate;
    }

    public Book getBook() {
        return book;
    }

    public Renter getRenter() {
        return renter;
    }

    public LocalDate getEndRentDate() {
        return endRentDate;
    }

    public double getFine() {
        double fine = 0.0;
        LocalDate today = LocalDate.now();
        if (today.isAfter(endRentDate)) {
            long days = endRentDate.until(today, ChronoUnit.DAYS);
            fine = PENYA * days;
        }
        return fine;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentInfo rentInfo = (RentInfo) o;
        return Objects.equals(book, rentInfo.book) && Objects.equals(renter, rentInfo.renter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(book, renter);
    }

    @Override
    public String toString() {
        return "RentInfo{" +
                "book=" + book +
                ", renter=" + renter +
                ", endRentDate=" + endRentDate +
                '}';
    }
}
