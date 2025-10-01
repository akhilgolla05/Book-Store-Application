package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.CreateOrderRequest;
import com.bookstore.order_service.domain.models.CreateOrderResponse;
import com.bookstore.order_service.domain.models.OrderCreatedEvent;
import com.bookstore.order_service.domain.models.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES =
            List.of("INDIA","USA","CANADA","UK","GERMANY");

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

    public void processNewOrders() {
        List<OrderEntity> newOrders = orderRepository.findByStatus(OrderStatus.NEW);
        logger.info("Processing {} New orders ", newOrders.size());
        for (OrderEntity order : newOrders) {
            this.process(order);
        }
    }

    private void process(OrderEntity order) {
        try{
            if(canBeDelivered(order)){
                logger.info("OrderNumber : {} can be Delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            }else{
                logger.info("OrderNumber : {} can't be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(OrderEventMapper.buildOrderCancelledEvent(order, "Can't deliver to this location!"));
            }
        }catch (RuntimeException ex){
            logger.info("Failed to process order with OrderNumber : {}", order.getOrderNumber());
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, ex.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERY_ALLOWED_COUNTRIES.contains(order.getDeliveryAddress().country().toUpperCase());
    }
}
