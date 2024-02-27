package com.epam.taskone.controller;

import com.epam.taskone.model.Product;
import com.epam.taskone.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    void testGetProductById() throws Exception {
        // Mocking ProductService to return a product when getProductById is called
        Product product = new Product(1L, "Test Product", "Description", 10.0, 5);
        when(productService.getProductById(1L)).thenReturn(Optional.of(product));

        // Perform GET request to /api/products/1
        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    void testGetProductByIdNotFound() throws Exception {
        // Mocking ProductService to return an empty optional when getProductById is called
        when(productService.getProductById(1L)).thenReturn(Optional.empty());

        // Perform GET request to /api/products/1
        mockMvc.perform(get("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testValidationExceptionHandlerWithValidator() throws Exception {
        // Prepare an invalid product request body
        String requestBody = "{\"name\":\"\", \"description\":\"\", \"price\":-10.0, \"quantity\":-5}";

        // Perform POST request to /api/products with validator enabled
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(content.contains("name: must not be blank"));
        assertTrue(content.contains("description: must not be blank"));
        assertTrue(content.contains("price: must be greater than 0"));
        assertTrue(content.contains("quantity: must be greater than 0"));
        // Verify that the ProductService method is not called
        verifyNoInteractions(productService);
    }

    @Test
    void testValidationExceptionHandler() throws Exception {
        // Prepare an invalid product request body
        String requestBody = "{\"name\":\"\", \"description\":\"\", \"price\":-10.0, \"quantity\":-5}";

        // Perform POST request to /api/products
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();

        assertTrue(content.contains("name: must not be blank"));
        assertTrue(content.contains("description: must not be blank"));
        assertTrue(content.contains("price: must be greater than 0"));
        assertTrue(content.contains("quantity: must be greater than 0"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        // Mocking ProductService to return a list of products when getAllProducts is called
        List<Product> productList = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0, 5),
                new Product(2L, "Product 2", "Description 2", 20.0, 10)
        );
        when(productService.getAllProducts()).thenReturn(productList);

        // Perform GET request to /api/products
        mockMvc.perform(get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Product 2"));
    }

    @Test
    void testAddProduct() throws Exception {
        // Prepare request body for POST request
        String requestBody = "{\"name\":\"New Product\", \"description\":\"New Description\", \"price\":15.0, \"quantity\":8}";

        // Mocking ProductService to return the newly created product
        Product newProduct = new Product(3L, "New Product", "New Description", 15.0, 8);
        when(productService.addProduct(any(Product.class))).thenReturn(newProduct);

        // Perform POST request to /api/products
        mockMvc.perform(MockMvcRequestBuilders.post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.name").value("New Product"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        // Prepare request body for PUT request
        String requestBody = "{\"id\":1, \"name\":\"Updated Product\", \"description\":\"Updated Description\", \"price\":25.0, \"quantity\":12}";

        // Mocking ProductService to return the updated product
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 25.0, 12);
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(updatedProduct);

        // Perform PUT request to /api/products/1
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.quantity").value(12));
    }

    @Test
    void testUpdateProductNotFound() throws Exception {
        // Prepare request body for PUT request
        String requestBody = "{\"id\":1, \"name\":\"Updated Product\", \"description\":\"Updated Description\", \"price\":25.0, \"quantity\":12}";

        // Mocking ProductService to return null when updateProduct is called
        when(productService.updateProduct(eq(1L), any(Product.class))).thenReturn(null);

        // Perform PUT request to /api/products/1
        mockMvc.perform(MockMvcRequestBuilders.put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteProduct() throws Exception {
        // Mocking ProductService for successful deletion
        doNothing().when(productService).deleteProduct(anyLong());

        // Perform DELETE request to /api/products/1
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}
