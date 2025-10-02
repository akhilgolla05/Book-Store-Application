package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.CreateOrderRequest;
import com.bookstore.order_service.domain.models.OrderDto;
import com.bookstore.order_service.domain.models.OrderItem;
import com.bookstore.order_service.domain.models.OrderStatus;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

//library : mapstruct
class OrderMapper {

    static OrderEntity toOrderEntity(CreateOrderRequest request) {

        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(request.customer());
        newOrder.setDeliveryAddress(request.deliveryAddress());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for(OrderItem item : request.items()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setName(item.name());
            orderItem.setQuantity(item.quantity());
            orderItem.setCode(item.code());
            orderItem.setPrice(item.price());
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;

    }

    static OrderDto convertToDTO(OrderEntity orderEntity){
        List<OrderItem> orderItems = orderEntity.getItems()
                .stream()
                .map(item->new OrderItem(item.getCode(), item.getName(), item.getPrice()
                , item.getQuantity()))
                .toList();

        return new OrderDto(
                orderEntity.getOrderNumber(),
                orderEntity.getUserName(),
                orderItems,
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                orderEntity.getStatus(),
                orderEntity.getComments(),
                orderEntity.getCreatedAt());
    }
}
