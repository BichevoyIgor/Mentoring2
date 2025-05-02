package ru.bichevoy.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.bichevoy.entity.Category;
import ru.bichevoy.entity.Product;

import java.util.List;
import java.util.Optional;


public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findProductByName(String name);

    List<Product> findAllProductsByCategory(Category category);

    List<Product> findAllProductsByOrderByPrice(PageRequest pageRequest);

    Optional<Product> findProductById(Long id);
}
