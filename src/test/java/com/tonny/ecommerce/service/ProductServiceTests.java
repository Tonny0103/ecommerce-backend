package com.tonny.ecommerce.service;

import com.tonny.ecommerce.DTO.PatchProductRequestDTO;
import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.ProductDTO;
import com.tonny.ecommerce.entity.Product;
import com.tonny.ecommerce.exception.ProductAlreadyExistsException;
import com.tonny.ecommerce.exception.ProductNotFoundException;
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
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
    void mustNotSaveProductWhenProductAlreadyExists() {
        PostProductRequestDTO product = new PostProductRequestDTO("title", "description", 10.0);

        when(productRepository.existsByTitleAndDescription(product.title(), product.description())).thenReturn(true);

        assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(product));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Must return all products")
    void mustReturnAllProducts() {
        when(productRepository.findAll()).thenReturn(ProductTestsUtils.fakeEntityList());

        List<ProductDTO> products = productService.getAllProducts();

        assertThat(products).hasSize(2).allSatisfy(p -> {
            assertThat(p.id()).isNotNull();
            assertThat(p.title()).isNotBlank();
            assertThat(p.description()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Must return empty list when no products found")
    void mustReturnEmptyListWhenThereAreNoProducts() {
        when(productRepository.findAll()).thenReturn(List.of());
        List<ProductDTO> products = productService.getAllProducts();

        assertThat(products).isEmpty();
        verify(productRepository).findAll();
    }

    @Test
    @DisplayName("Must edit product with new valid title")
    void mustEditProductWithNewValidTitle() {
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Product product = new Product(id, "title", "description", 0.0, 0, 10.0, now, now);
        Product edited = new Product(id, "new title", "description", 0.0, 0, 10.0, now, LocalDateTime.now());

        when(productRepository.findById(id)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(edited);

        ProductDTO response = productService.patchProduct(id, new PatchProductRequestDTO("new title", null, null));

        verify(productRepository).save(edited);
    }

    @Test
    @DisplayName("Must throw exception when product not found")
    void mustThrowExceptionWhenProductNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(ProductNotFoundException.class, () -> productService.patchProduct(id, new PatchProductRequestDTO("new title", null, null)));
    }

    @Test
    @DisplayName("Must delete product")
    void mustDeleteProduct() {
        UUID id = UUID.randomUUID();

        when(productRepository.existsById(id)).thenReturn(true);
        doNothing().when(productRepository).deleteById(id);

        productService.deleteById(id);

        verify(productRepository).deleteById(id);
    }
}
