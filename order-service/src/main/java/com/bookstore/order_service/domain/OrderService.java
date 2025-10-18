package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.*;
import java.util.List;
import java.util.Optional;
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
    private static final List<String> DELIVERY_ALLOWED_COUNTRIES = List.of("INDIA", "USA", "CANADA", "UK", "GERMANY");

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
        try {
            if (canBeDelivered(order)) {
                logger.info("OrderNumber : {} can be Delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.save(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                logger.info("OrderNumber : {} can't be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.save(
                        OrderEventMapper.buildOrderCancelledEvent(order, "Can't deliver to this location!"));
            }
        } catch (RuntimeException ex) {
            logger.info("Failed to process order with OrderNumber : {}", order.getOrderNumber());
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.save(OrderEventMapper.buildOrderErrorEvent(order, ex.getMessage()));
        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        logger.info("OrderNumber : {} delivery check", order.getOrderNumber());
        return DELIVERY_ALLOWED_COUNTRIES.contains(
                order.getDeliveryAddress().country().toUpperCase());
    }

    public List<OrderSummary> getOrders(String username) {
        logger.info("OrderService :: getOrders");
        return orderRepository.findByUserName(username);
    }

    public Optional<OrderDto> findUserOrder(String orderNumber, String username) {
        return orderRepository
                .findByUserNameAndOrderNumber(username, orderNumber)
                .map(OrderMapper::convertToDTO);
    }

    /*
        it fires 2 queries first it ftech the details and then order items, by default its lazy loading, if we use default Jpa Methods
        Hibernate: select oe1_0.id,oe1_0.comments,oe1_0.created_at,oe1_0.customer_name,oe1_0.customer_email,oe1_0.customer_phone,oe1_0.delivery_address_line1,oe1_0.delivery_address_line2,oe1_0.delivery_address_city,oe1_0.delivery_address_state,oe1_0.delivery_address_zip_code,oe1_0.delivery_address_country,oe1_0.order_number,oe1_0.status,oe1_0.updated_at,oe1_0.username from orders oe1_0 where oe1_0.username=? and oe1_0.order_number=?
    Hibernate: select i1_0.order_id,i1_0.id,i1_0.code,i1_0.name,i1_0.price,i1_0.quantity from order_items i1_0 where i1_0.order_id=?

         */
}
