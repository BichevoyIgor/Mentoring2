package ru.bichevoy.dao;

import ru.bichevoy.entity.Customer;

import java.util.Optional;

public interface CustomerDAO {
    Optional<Customer> findById(long id);

    long add(String firstName, String LastName);

}
