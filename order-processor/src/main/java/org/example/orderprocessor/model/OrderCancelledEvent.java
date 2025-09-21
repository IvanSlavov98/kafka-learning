package org.example.orderprocessor.model;

import java.time.Instant;

public record OrderCancelledEvent(
        String orderId,
        String reason,
        Instant cancelledAt
) {
}