package com.bookstore.order_service.domain.models;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

public record CreateOrderRequest(
        @NotEmpty(message = "Order Items Cannot be Empty") Set<OrderItem> items,
        @Valid Customer customer,
        @Valid DeliveryAddress deliveryAddress) {
}
