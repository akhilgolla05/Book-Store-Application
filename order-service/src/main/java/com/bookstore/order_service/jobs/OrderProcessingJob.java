package com.bookstore.order_service.jobs;

import com.bookstore.order_service.domain.OrderService;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class OrderProcessingJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderProcessingJob.class);

    private final OrderService orderService;

    @Scheduled(cron = "${orders.new-orders-job-cron}")
    @SchedulerLock(name = "processNewOrders")
    public void processNewOrders(){
        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        logger.info("Processing New orders at : {}", Instant.now());
        orderService.processNewOrders();
    }

}
