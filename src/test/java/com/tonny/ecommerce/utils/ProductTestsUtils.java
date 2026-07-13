package com.tonny.ecommerce.utils;

import com.tonny.ecommerce.DTO.PostProductRequestDTO;
import com.tonny.ecommerce.DTO.ProductDTO;
import com.tonny.ecommerce.entity.Product;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class ProductTestsUtils {

    public static Product fakeEntity() {
        return new Product(UUID.randomUUID(), "title", "description", 0.0, 0, 10.0, LocalDateTime.now(), LocalDateTime.now());
    }

    public static List<Product> fakeEntityList() {
        return List.of(
                new Product(UUID.randomUUID(), "title1", "description1", 0.0, 0, 10.0, LocalDateTime.now(), LocalDateTime.now()),
                new Product(UUID.randomUUID(), "title2", "description2", 0.0, 0, 20.0, LocalDateTime.now(), LocalDateTime.now())
        );
    }

    public static ProductDTO fakeResponse(PostProductRequestDTO request) {
        return new ProductDTO(
                UUID.randomUUID(),
                request.title(),
                request.description(),
                0.0,
                0,
                request.price(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static List<ProductDTO> fakeResponseList() {
        return List.of(
                fakeResponse(new PostProductRequestDTO("title1", "description1", 10.0)),
                fakeResponse(new PostProductRequestDTO("title2", "description2", 20.0)),
                fakeResponse(new PostProductRequestDTO("title3", "description3", 30.0))
        );
    }
}
