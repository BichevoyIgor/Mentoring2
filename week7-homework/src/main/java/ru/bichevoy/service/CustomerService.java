package ru.bichevoy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dao.CustomerDAO;
import ru.bichevoy.entity.Customer;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerDAO customerDAO;

    public void add(Customer customer) {
        long id = customerDAO.add(customer.getFirstName().trim(), customer.getLastName().trim());
        customer.setId(id);
    }

    public Optional<Customer> findCustomerById(long id) {
        return customerDAO.findById(id);
    }
}
