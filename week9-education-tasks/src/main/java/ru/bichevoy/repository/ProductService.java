package ru.bichevoy.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.bichevoy.dto.mapper.ProductMapper;
import ru.bichevoy.dto.response.ProductCreateDTO;
import ru.bichevoy.dto.response.ProductResponseDTO;
import ru.bichevoy.entity.Product;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;

    public List<ProductResponseDTO> getAllProductsDTO() {
        return productRepository.findAll()
                .stream()
                .map(productMapper::getProductResponseDTO)
                .toList();
    }

    public ProductResponseDTO saveProductFromPOST(ProductCreateDTO productDTO) {
        Product product = productMapper.getProductFromDTO(productDTO);
        productRepository.save(product);
        return productMapper.getProductResponseDTO(product);
    }

    public ProductResponseDTO updateProductFromPOST(ProductCreateDTO productDTO) {
        Product product = productMapper.getProductFromDTO(productDTO);
        productRepository.save(product);
        return productMapper.getProductResponseDTO(product);
    }

    public Optional<ProductResponseDTO> findProductDTOById(Long id) {
        Optional<Product> productById = findProductById(id);
        return productById.map(productMapper::getProductResponseDTO);
    }

    public Optional<Product> findProductById(Long id) {
        return productRepository.findProductById(id);
    }

    public void deleteProductById(Long id) {
        Optional<Product> productOpt = findProductById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            product.getCategory().getProductList().remove(product);
            productRepository.deleteById(id);
        }
    }
}
