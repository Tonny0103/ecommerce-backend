package com.tonny.ecommerce.DTO;

import java.time.LocalDateTime;

public record ErrorResponseDTO(
        int status,
        String titulo,
        String mensagem,
        LocalDateTime timestamp
) {
}
