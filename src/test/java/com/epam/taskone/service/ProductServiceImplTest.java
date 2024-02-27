package com.epam.taskone.service;

import com.epam.taskone.model.Product;
import com.epam.taskone.repository.ProductRepository;
import com.epam.taskone.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testAddProduct() {
        Product productToAdd = new Product(1L, "Test Product", "Description", 10.0, 5);
        when(productRepository.save(any(Product.class))).thenReturn(productToAdd);

        Product addedProduct = productService.addProduct(productToAdd);
        assertEquals(productToAdd, addedProduct);
        verify(productRepository, times(1)).save(productToAdd);
    }

    @Test
    void testGetAllProducts() {
        List<Product> productList = Arrays.asList(
                new Product(1L, "Product 1", "Description 1", 10.0, 5),
                new Product(2L, "Product 2", "Description 2", 20.0, 10)
        );
        when(productRepository.findAll()).thenReturn(productList);

        List<Product> retrievedProducts = productService.getAllProducts();
        assertEquals(2, retrievedProducts.size());
        assertEquals("Product 1", retrievedProducts.get(0).getName());
        assertEquals("Product 2", retrievedProducts.get(1).getName());
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void testGetProductById() {
        Product product = new Product(1L, "Product 1", "Description 1", 10.0, 5);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Optional<Product> retrievedProduct = productService.getProductById(1L);
        assertTrue(retrievedProduct.isPresent());
        assertEquals(product.getName(), retrievedProduct.get().getName());
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateProduct() {
        Product existingProduct = new Product(1L, "Existing Product", "Description", 10.0, 5);
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 20.0, 8);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        Product result = productService.updateProduct(1L, updatedProduct);
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getDescription(), result.getDescription());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getQuantity(), result.getQuantity());
        verify(productRepository, times(1)).findById(1L);
        verify(productRepository, times(1)).save(updatedProduct);
    }

    @Test
    void testUpdateProductWhenNotFound() {
        // Mocking getProductById to return an empty Optional
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Attempt to update a product that does not exist
        Product updatedProduct = new Product(1L, "Updated Product", "Updated Description", 25.0, 12);
        Product result = productService.updateProduct(1L, updatedProduct);

        // Verify that the result is null
        assertNull(result);
    }

    @Test
    void testDeleteProduct() {
        // Mocking deleteById method of productRepository
        doNothing().when(productRepository).deleteById(anyLong());

        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
