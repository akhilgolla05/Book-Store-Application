package com.bookstore.order_service.domain;

import com.bookstore.order_service.ApplicationProperties;
import com.bookstore.order_service.domain.models.OrderCancelledEvent;
import com.bookstore.order_service.domain.models.OrderCreatedEvent;
import com.bookstore.order_service.domain.models.OrderDeliveredEvent;
import com.bookstore.order_service.domain.models.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisher {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    public void publish(OrderCreatedEvent event){
        this.send(properties.newOrdersQueue(), event);
    }
    public void publish(OrderDeliveredEvent event){
        this.send(properties.deliveredOrdersQueue(), event);
    }
    public void publish(OrderCancelledEvent event){
        this.send(properties.cancelledOrdersQueue(), event);
    }
    public void publish(OrderErrorEvent event){
        this.send(properties.errorOrdersQueue(), event);
    }

    //we may use diff events : object is recommended(generic)
    private void send(String routingKey, Object event) {
        rabbitTemplate.convertAndSend(properties.orderEventsExchange(), routingKey, event);
        logger.info("Published order event {}", event);
    }
}
