package com.tonny.ecommerce.service;

import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.ProductDTO;
import com.tonny.ecommerce.entity.Product;
import com.tonny.ecommerce.exception.ProductAlreadyExistsException;
import com.tonny.ecommerce.repository.ProductRepository;
import com.tonny.ecommerce.utils.ProductTestsUtils;
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
        Product saved = new Product(UUID.randomUUID(), product.title(), product.description(), 0.0, 0, product.price(), LocalDateTime.now(), LocalDateTime.now());

        when(productRepository.existsByTitleAndDescription(product.title(), product.description())).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        ProductDTO response = productService.createProduct(product);

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
        when(productRepository.findAll()).thenReturn(ProductTestsUtils.fakeEntityList());

        List<ProductDTO> products = productService.getAllProducts();

        assertEquals(2, products.size());
    }


}
