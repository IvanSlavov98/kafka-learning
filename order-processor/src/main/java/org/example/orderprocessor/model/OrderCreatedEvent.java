package org.example.orderprocessor.model;

import java.math.BigDecimal;
import java.time.Instant;

public record OrderCreatedEvent(
        String orderId,
        String customerId,
        BigDecimal totalAmount,
        String currency,
        Instant createdAt
) {
}