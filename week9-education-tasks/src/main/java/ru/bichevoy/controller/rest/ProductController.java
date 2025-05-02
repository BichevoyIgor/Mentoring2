package ru.bichevoy.controller.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bichevoy.dto.response.ProductCreateDTO;
import ru.bichevoy.dto.response.ProductResponseDTO;
import ru.bichevoy.repository.ProductService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductResponseDTO> getProducts() {
        return productService.getAllProductsDTO();
    }

    @GetMapping("/{id}")
    public ProductResponseDTO getProductById(@PathVariable Long id) {
        return (productService.findProductDTOById(id))
                .orElseThrow(() -> new NoSuchElementException(String.format("id=%d not found", id))
                );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ProductResponseDTO createProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        return productService.saveProductFromPOST(productCreateDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasAuthority('admin')")
    public ProductResponseDTO changeProduct(@Valid @RequestBody ProductCreateDTO productDTO) {
        return productService.updateProductFromPOST(productDTO);
    }
}
