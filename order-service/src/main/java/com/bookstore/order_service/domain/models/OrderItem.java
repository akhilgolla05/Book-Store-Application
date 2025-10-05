package com.bookstore.order_service.domain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record OrderItem(
        @NotBlank(message = "Code is Required") String code,
        @NotBlank(message = "Name is Required") String name,
        @NotBlank(message = "Price is Required") BigDecimal price,
        @NotBlank(message = "Quantity is Required") @Min(1) Integer quantity) {}
