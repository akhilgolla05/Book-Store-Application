package com.bookstore.order_service.domain.models;

import java.time.LocalDateTime;
import java.util.Set;

public record OrderCancelledEvent(
        String eventId,
        String orderNumber,
        Set<OrderItem> items,
        Customer customer,
        DeliveryAddress deliveryAddress,
        String reason,
        LocalDateTime createdAt) {
}
