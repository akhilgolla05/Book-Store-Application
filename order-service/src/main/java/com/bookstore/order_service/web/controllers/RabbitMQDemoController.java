package com.bookstore.order_service.web.controllers;

import com.bookstore.order_service.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

/*
{
    "routingKey":"delivered-orders",
    "payload":{
        "content":"Order Delivered with id:1234"
    }
}
 */
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class RabbitMQDemoController {

    private final RabbitTemplate rabbitTemplate;
    private final ApplicationProperties properties;

    @PostMapping("/send")
    public void sendTestMessage(@RequestBody MyMessage message){
        rabbitTemplate.convertAndSend(
                properties.orderEventsExchange(),
                message.routingKey(),
                message.payload()
        );
    }
}

record MyMessage(String routingKey, MyPayload payload){}
record MyPayload(String content){}
