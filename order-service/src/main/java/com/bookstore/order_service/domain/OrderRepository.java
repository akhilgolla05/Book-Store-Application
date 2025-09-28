package com.bookstore.order_service.domain;

import org.springframework.data.jpa.repository.JpaRepository;

//OrderItem is a part of Order(aggregates), we ar creating for order repo
interface OrderRepository extends JpaRepository<OrderEntity, Long> {
}
