package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.CreateOrderRequest;
import com.bookstore.order_service.domain.models.CreateOrderResponse;
import com.bookstore.order_service.domain.models.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final OrderValidator orderValidator;
    private final OrderEventService orderEventService;

    public CreateOrderResponse createOrder(String username, CreateOrderRequest request) {
        orderValidator.validate(request);
        OrderEntity newOrder = OrderMapper.toOrderEntity(request);
        newOrder.setUserName(username);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        logger.info("Order created Number : {}", savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.save(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

}
