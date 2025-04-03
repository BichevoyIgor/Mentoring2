package ru.bichevoy.entity;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RentDetail {
    private long id;
    private Customer customer;
    private Book book;
    private LocalDate startDate;
    private LocalDate endDate;
}
