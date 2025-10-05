package com.bookstore.order_service.domain;

import com.bookstore.order_service.clients.catalog.Product;
import com.bookstore.order_service.clients.catalog.ProductServiceClient;
import com.bookstore.order_service.domain.models.CreateOrderRequest;
import com.bookstore.order_service.domain.models.OrderItem;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderValidator {
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final ProductServiceClient client;

    void validate(CreateOrderRequest request) {
        logger.info("Validating order");
        Set<OrderItem> items = request.items();
        for (OrderItem item : items) {
            logger.info("Validating order item {}", item.code());
            Product product = client.getProductByCode(item.code())
                    .orElseThrow(() -> new InvalidOrderException("Invalid Product Code" + item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                logger.error(
                        "Product price not matching, actual price is {}, received price : {}",
                        product.price(),
                        item.price());
                throw new InvalidOrderException("Product price not matching!");
            }
        }
    }
}
