package com.bookstore.order_service.jobs;

import com.bookstore.order_service.domain.OrderEventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OrderEventPublishingJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublishingJob.class);

    private final OrderEventService orderEventService;

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    public void publishOrderEvents(){
        logger.info("Publishing Order Events  At: {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
