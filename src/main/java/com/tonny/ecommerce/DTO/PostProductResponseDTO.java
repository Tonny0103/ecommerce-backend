package com.tonny.ecommerce.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostProductResponseDTO(
        UUID id,
        String title,
        String description,
        Double reviews,
        Integer reviewsCount,
        Double price,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
