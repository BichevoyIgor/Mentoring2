package ru.bichevoy.dto.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.bichevoy.dto.response.ProductCreateDTO;
import ru.bichevoy.dto.response.ProductResponseDTO;
import ru.bichevoy.entity.Category;
import ru.bichevoy.entity.Product;
import ru.bichevoy.repository.CategoryRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductMapper {

    private final CategoryRepository categoryRepository;

    public ProductResponseDTO getProductResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getCategory().getName(),
                product.getCategory().getId()
        );
    }

    public Product getProductFromDTO(ProductCreateDTO productDTO) {
        Product product = new Product(productDTO.name(), productDTO.price());
        product.setId(productDTO.id());
        Optional<Category> category = categoryRepository.findCategoryById(productDTO.categoryId());
        category.ifPresent(product::setCategory);
        return product;
    }
}
