package org.example.ordersapi.model.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateOrderRequest(
        @NotBlank String orderId,
        @NotBlank String customerId,
        @NotNull @DecimalMin("0.0") BigDecimal totalAmount,
        @NotBlank String currency
) {}