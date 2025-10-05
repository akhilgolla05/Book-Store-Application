package com.bookstore.order_service.web.controllers;

import lombok.extern.slf4j.Slf4j;

// @Service
@Slf4j
public class RabbitMQDemoListner {

    // @RabbitListener(queues = "${orders.new-orders-queue}")
    public void handleNewOrder(MyPayload payload) {
        log.info("Received new order: {}", payload);
    }

    // @RabbitListener(queues = "${orders.delivered-orders-queue}")
    public void handleDeliveredOrder(MyPayload payload) {
        log.info("Delivered order: {}", payload);
    }
}
