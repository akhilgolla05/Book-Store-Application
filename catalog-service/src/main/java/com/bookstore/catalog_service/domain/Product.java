package com.bookstore.catalog_service.domain;

import java.math.BigDecimal;

public record Product(
        String code,
        String name,
        String description,
        String url,
        BigDecimal price) {
}
