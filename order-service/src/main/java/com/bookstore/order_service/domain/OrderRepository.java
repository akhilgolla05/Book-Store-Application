package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

//OrderItem is a part of Order(aggregates), we ar creating for order repo
interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findByStatus(OrderStatus orderStatus);

    default void updateOrderStatus(String orderNumber, OrderStatus orderStatus){
        OrderEntity entity = this.findByOrderNumber(orderNumber).orElseThrow();
        entity.setStatus(orderStatus);
        this.save(entity);
    }

    Optional<OrderEntity> findByOrderNumber(String orderNumber);
}
