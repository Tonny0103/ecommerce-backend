package com.tonny.ecommerce.DTO;

public record PatchProductRequestDTO(
        String title,
        String description,
        Double price
) {
}
