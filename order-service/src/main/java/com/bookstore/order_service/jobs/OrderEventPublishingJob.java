package com.bookstore.order_service.jobs;

import com.bookstore.order_service.domain.OrderEventService;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import net.javacrumbs.shedlock.core.LockAssert;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublishingJob {

    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublishingJob.class);

    private final OrderEventService orderEventService;

    @Scheduled(cron = "${orders.publish-order-events-job-cron}")
    @SchedulerLock(name = "publishOrderEvents")
    public void publishOrderEvents() {
        // To assert that the lock is held (prevents misconfiguration errors)
        LockAssert.assertLocked();
        logger.info("Publishing Order Events  At: {}", Instant.now());
        orderEventService.publishOrderEvents();
    }
}
