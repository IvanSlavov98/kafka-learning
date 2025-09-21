package org.example.ordersapi.model.events;

import java.time.Instant;

public record OrderCancelledEvent(
        String orderId,
        String reason,
        Instant cancelledAt
) {
}