package com.tonny.ecommerce.service;

import com.tonny.ecommerce.DTO.PatchProductRequestDTO;
import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.ProductDTO;
import com.tonny.ecommerce.entity.Product;
import com.tonny.ecommerce.exception.ProductAlreadyExistsException;
import com.tonny.ecommerce.exception.ProductNotFoundException;
import com.tonny.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDTO createProduct(PostProductRequestDTO productRequestDTO) {
        if (productRepository.existsByTitleAndDescription(productRequestDTO.title(), productRequestDTO.description())) {
            throw new ProductAlreadyExistsException(productRequestDTO.title());
        }

        Product product = new Product(productRequestDTO.title(), productRequestDTO.description(), productRequestDTO.price());
        Product savedProduct = productRepository.save(product);

        return new ProductDTO(
                savedProduct.getId(),
                savedProduct.getTitle(),
                savedProduct.getDescription(),
                savedProduct.getReviews(),
                savedProduct.getReviewsCount(),
                savedProduct.getPrice(),
                savedProduct.getCreatedAt(),
                savedProduct.getUpdatedAt()
        );
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(product -> new ProductDTO(
                product.getId(),
                product.getTitle(),
                product.getDescription(),
                product.getReviews(),
                product.getReviewsCount(),
                product.getPrice(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        )).toList();
    }

    public ProductDTO patchProduct(UUID id, PatchProductRequestDTO request) {
        Optional<Product> product = productRepository.findById(id);

        if (product.isEmpty()) {
            throw new ProductNotFoundException(String.format("Product with id %s not found", id));
        }

        if (request.title() != null) product.get().setTitle(request.title());
        if (request.description() != null) product.get().setDescription(request.description());
        if (request.price() != null) product.get().setPrice(request.price());

        Product edited = productRepository.save(product.get());

        return new ProductDTO(
                edited.getId(),
                edited.getTitle(),
                edited.getDescription(),
                edited.getReviews(),
                edited.getReviewsCount(),
                edited.getPrice(),
                edited.getCreatedAt(),
                edited.getUpdatedAt()
        );
    }

    public void deleteById(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(String.format("Product with id %s not found", id));
        }

        productRepository.deleteById(id);
    }
}
