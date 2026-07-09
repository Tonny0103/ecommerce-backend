package com.tonny.ecommerce.DTO;

public record PostProductRequestDTO(
        String title,
        String description,
        Double price
) {
}
