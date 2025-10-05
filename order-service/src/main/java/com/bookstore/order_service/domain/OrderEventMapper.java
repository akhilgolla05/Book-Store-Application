package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.*;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderEventMapper {

    static OrderCreatedEvent buildOrderCreatedEvent(OrderEntity orderEntity) {
        return new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                orderEntity.getCreatedAt());
    }

    static OrderDeliveredEvent buildOrderDeliveredEvent(OrderEntity orderEntity) {
        return new OrderDeliveredEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                orderEntity.getCreatedAt());
    }

    static OrderCancelledEvent buildOrderCancelledEvent(OrderEntity orderEntity, String reason) {
        return new OrderCancelledEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                reason,
                orderEntity.getCreatedAt());
    }

    static OrderErrorEvent buildOrderErrorEvent(OrderEntity orderEntity, String reason) {
        return new OrderErrorEvent(
                UUID.randomUUID().toString(),
                orderEntity.getOrderNumber(),
                getOrderItems(orderEntity),
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                reason,
                orderEntity.getCreatedAt());
    }

    private static Set<OrderItem> getOrderItems(OrderEntity order) {
        return order.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());
    }
}
