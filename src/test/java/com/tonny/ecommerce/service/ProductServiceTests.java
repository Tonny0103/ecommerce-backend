package com.tonny.ecommerce.service;

import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.PostProductResponseDTO;
import com.tonny.ecommerce.entity.Product;
import com.tonny.ecommerce.exception.ProductAlreadyExistsException;
import com.tonny.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Must create product with unique title and description")
    void mustCreateProductWithUniqueTitleAndDescription() {
        PostProductRequestDTO product = new PostProductRequestDTO("title", "description", 10.0);
        Product saved = fakeSavedProduct(product);

        when(productRepository.existsByTitleAndDescription(product.title(), product.description())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        PostProductResponseDTO response = productService.createProduct(product);

        verify(productRepository).existsByTitleAndDescription(product.title(), product.description());
        verify(productRepository).save(any(Product.class));
        assertThat(response).isNotNull();
        assertThat(response.id()).isNotNull();
        assertThat(response.title()).isEqualTo(product.title());
        assertThat(response.description()).isEqualTo(product.description());
        assertThat(response.price()).isEqualTo(product.price());
        assertThat(response.reviews()).isNotNull().isEqualTo(0);
        assertThat(response.reviewsCount()).isNotNull().isEqualTo(0);
        assertThat(response.createdAt()).isNotNull();
    }

    @Test
    @DisplayName("Must throw exception when product already exists")
    void mustThrowExceptionWhenProductAlreadyExists() {
        PostProductRequestDTO product = new PostProductRequestDTO("title", "description", 10.0);

        when(productRepository.existsByTitleAndDescription(product.title(), product.description())).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(product));
        verify(productRepository).existsByTitleAndDescription(product.title(), product.description());
    }

    @Test
    @DisplayName("Must return all products")
    void mustReturnAllProducts() {
        PostProductRequestDTO product1 = new PostProductRequestDTO("title1", "description1", 10.0);
        PostProductRequestDTO product2 = new PostProductRequestDTO("title2", "description2", 20.0);
        Product savedProduct1 = fakeSavedProduct(product1);
        Product savedProduct2 = fakeSavedProduct(product2);

        when(productRepository.findAll()).thenReturn(java.util.List.of(savedProduct1, savedProduct2));

        List<Product> products = productService.getAllProducts();

        assertEquals(2, products.size());
        assertEquals(savedProduct1, products.get(0));
        assertEquals(savedProduct2, products.get(1));
    }

    private Product fakeSavedProduct(PostProductRequestDTO product) {
        Product saved = new Product();
        saved.setId(UUID.randomUUID());
        saved.setTitle(product.title());
        saved.setDescription(product.description());
        saved.setPrice(product.price());
        saved.setReviews(0.0);
        saved.setReviewsCount(0);
        saved.setCreatedAt(LocalDateTime.now());
        return saved;
    }
}
