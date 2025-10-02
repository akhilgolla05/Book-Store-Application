package com.bookstore.order_service.domain.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderDto(
        String orderNumber,
        String userName,
        List<OrderItem> items,
        Customer customer,
        DeliveryAddress deliveryAddress,
        OrderStatus status,
        String comments,
        LocalDateTime createdAt) {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public BigDecimal getTotalPrice() {

       return items.stream()
                .map(item->item.price().multiply(BigDecimal.valueOf(item.quantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
