package com.bookstore.order_service.web.controllers;

import com.bookstore.order_service.domain.OrderService;
import com.bookstore.order_service.domain.SecurityService;
import com.bookstore.order_service.domain.models.CreateOrderRequest;
import com.bookstore.order_service.domain.models.CreateOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;
    private final SecurityService securityService;

    //for Test case for controller : need to write for controller, because we are checking the missing fields, not req integration
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CreateOrderResponse createOrder(@Valid @RequestBody CreateOrderRequest request){
        String username = securityService.getLoggedInUserName();
        logger.info("Creating order for user: {}", username);
        return orderService.createOrder(username, request);
    }
}


/*

{
    "customer":{
        "name":"Munna",
        "email":"munna@gmail.com",
        "phone":"8545712558"
    },
    "deliveryAddress":{
        "addressLine1":"222 kukatpally",
        "addressLine2":"kphb",
        "city":"Hyderabad",
        "state":"Telangana",
        "zipCode":"500072",
        "country":"India"
    },
    "items":[
        {
            "code":"P100",
            "name":"product 1",
            "price":"25.50",
            "quantity":1
        },
        {
            "code":"P101",
            "name":"product 2",
            "price":"35.50",
            "quantity":2
        },
        {
            "code":"P102",
            "name":"product 3",
            "price":"45.50",
            "quantity":5
        }
    ]
}
 */