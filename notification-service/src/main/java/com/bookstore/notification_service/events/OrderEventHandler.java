package com.bookstore.notification_service.events;

import com.bookstore.notification_service.domain.NotificationService;
import com.bookstore.notification_service.domain.OrderEventEntity;
import com.bookstore.notification_service.domain.OrderEventRepository;
import com.bookstore.notification_service.domain.models.OrderCancelledEvent;
import com.bookstore.notification_service.domain.models.OrderCreatedEvent;
import com.bookstore.notification_service.domain.models.OrderDeliveredEvent;
import com.bookstore.notification_service.domain.models.OrderErrorEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final NotificationService notificationService;
    private final OrderEventRepository orderEventRepository;

    @RabbitListener(queues = "${notifications.new-orders-queue}")
    public void handleCreateOrderEventListener(OrderCreatedEvent event) {
        log.info("Received order created event {}", event);
        if (!orderEventRepository.existsByEventId(event.eventId())) {
            notificationService.sendOrderCreatedNotification(event);
            orderEventRepository.save(new OrderEventEntity(event.eventId()));
        } else {
            log.warn("Received duplicate OrderEvent with  {}", event.eventId());
        }
    }

    @RabbitListener(queues = "${notifications.delivered-orders-queue}")
    public void handleDeliveredOrderEventListener(OrderDeliveredEvent event) {
        log.info("Received order Delivered event {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderEvent with  {}", event.eventId());
            return;
        }
        notificationService.sendOrderDeliveredNotification(event);
        orderEventRepository.save(new OrderEventEntity(event.eventId()));
    }

    @RabbitListener(queues = "${notifications.cancelled-orders-queue}")
    public void handleCancelledOrderEventListener(OrderCancelledEvent event) {
        log.info("Received order Cancelled event {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderEvent with  {}", event.eventId());
            return;
        }
        notificationService.sendOrderCancelledNotification(event);
        orderEventRepository.save(new OrderEventEntity(event.eventId()));
    }

    @RabbitListener(queues = "${notifications.error-orders-queue}")
    public void handleErrorOrderEventListener(OrderErrorEvent event) {
        log.info("Received order Error event {}", event);
        if (orderEventRepository.existsByEventId(event.eventId())) {
            log.warn("Received duplicate OrderEvent with  {}", event.eventId());
            return;
        }
        notificationService.sendOrderErrorEventNotification(event);
        orderEventRepository.save(new OrderEventEntity(event.eventId()));
    }
}
