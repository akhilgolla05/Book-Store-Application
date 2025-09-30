package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.OrderCreatedEvent;
import com.bookstore.order_service.domain.models.OrderItem;

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

    private static Set<OrderItem> getOrderItems(OrderEntity order){
        return order.getItems().stream()
                .map(item-> new OrderItem(item.getCode(),item.getName(),
                        item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());
    }
}
