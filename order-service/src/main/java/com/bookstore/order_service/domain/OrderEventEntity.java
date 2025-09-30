package com.bookstore.order_service.domain;

import com.bookstore.order_service.domain.models.OrderEventType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_events")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_event_id_generator")
    @SequenceGenerator(name = "order_event_id_generator", sequenceName = "order_event_id_seq")
    private Long id;

    @Column(nullable = false)
    private String orderNumber;

    @Column(nullable = false, unique = true)
    private String eventId;

    @Enumerated(EnumType.STRING)
    private OrderEventType eventType;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false, name = "created_at", updatable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
