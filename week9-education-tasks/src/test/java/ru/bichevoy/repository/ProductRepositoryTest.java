package ru.bichevoy.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import ru.bichevoy.entity.Category;
import ru.bichevoy.entity.Product;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void findProductByNameTest() {
        Optional<Product> laptop = productRepository.findProductByName("Laptop Acer");
        assertTrue(laptop.isPresent());

        Optional<Product> mouse = productRepository.findProductByName("mouse");
        assertFalse(mouse.isPresent());
    }

    @Test
    void findAllProductsByCategoryTest() {
        Optional<Category> categoryById = categoryRepository.findCategoryById(1L);
        Product product = new Product("Sound bar", 55.99);
        product.setCategory(categoryRepository.findCategoryById(1L).get());
        entityManager.persist(product);
        entityManager.flush();
        if (categoryById.isPresent()) {
            List<Product> allProductsByCategory = productRepository.findAllProductsByCategory(categoryById.get());
            assertEquals(6, allProductsByCategory.size());
        }
    }

    @Test
    void findAllProductsByOrderByPriceTest() {
        PageRequest pageRequest = PageRequest.of(0, 5);
        List<Product> allByOrderByPrice = productRepository.findAllProductsByOrderByPrice(pageRequest);
        assertEquals(5, allByOrderByPrice.size());
    }
}