package com.bookstore.order_service.domain;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(String message) {
        super(message);
    }

    public static OrderNotFoundException forOrderNumbers(String orderNumber) {
        return new OrderNotFoundException("Order Not Found with Order Number: " + orderNumber);
    }
}
