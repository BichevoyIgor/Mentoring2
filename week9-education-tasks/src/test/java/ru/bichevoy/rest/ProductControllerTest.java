package ru.bichevoy.rest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import ru.bichevoy.controller.rest.ProductController;
import ru.bichevoy.dto.response.ProductCreateDTO;
import ru.bichevoy.dto.response.ProductResponseDTO;
import ru.bichevoy.repository.ProductService;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void getProducts() throws Exception {
        List<ProductResponseDTO> responseDTOS = List.of(
                new ProductResponseDTO(1L, "Laptop Acer", 1000.0, "Electronics", 1L),
                new ProductResponseDTO(2L, "Laptop HP", 2000.0, "Electronics", 1L),
                new ProductResponseDTO(3L, "Laptop Apple", 5000.0, "Electronics", 1L)
        );
        when(productService.getAllProductsDTO()).thenReturn(responseDTOS);

        mockMvc.perform(get("/api/v1/products"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getProductById() throws Exception {
        when(productService.findProductDTOById(1L))
                .thenReturn(Optional.of(new ProductResponseDTO(1L, "Laptop Acer", 1000.0, "Electronics", 1L)));

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Laptop Acer"))
                .andExpect(jsonPath("$.price").value(1000.0));
    }

    @Test
    void createProduct() throws Exception {
        when(productService.saveProductFromPOST(new ProductCreateDTO(null, "Laptop Apple", 1500.00, "Electronics", 1L)))
                .thenReturn(new ProductResponseDTO(1L, "Laptop Apple", 1500.00, "Electronics", 1L));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Laptop Apple",
                                  "price": 1500.00,
                                  "categoryName": "Electronics",
                                  "categoryId": 1
                                }
                                """))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deleteProduct() throws Exception {
        doNothing().when(productService).deleteProductById(1L);

        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isOk());
    }

    @Test
    void changeProduct() throws Exception {

        ProductCreateDTO productDTO = new ProductCreateDTO(1L,
                "Laptop Apple",
                1300.00,
                "Electronics",
                1L);
        ProductResponseDTO responseDTO = new ProductResponseDTO(
                1L,
                "Laptop Apple",
                1300.00,
                "Electronics",
                1L
        );
        when(productService.updateProductFromPOST(productDTO)).thenReturn(responseDTO);

        mockMvc.perform(put("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "id": 1,
                                  "name": "Laptop Apple",
                                  "price": 1300.00,
                                  "categoryName": "Electronics",
                                  "categoryId": 1
                                }
                                """))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.price").value(1300));
    }
}