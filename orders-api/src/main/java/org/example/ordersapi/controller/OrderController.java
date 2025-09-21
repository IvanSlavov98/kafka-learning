package org.example.ordersapi.controller;

import org.example.ordersapi.model.events.OrderCancelledEvent;
import org.example.ordersapi.model.events.OrderCreatedEvent;
import org.example.ordersapi.model.requests.CancelOrderRequest;
import org.example.ordersapi.model.requests.CreateOrderRequest;
import org.example.ordersapi.service.OrderEventProducer;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderEventProducer orderEventProducer;

    public OrderController(OrderEventProducer orderEventProducer) {
        this.orderEventProducer = orderEventProducer;
    }

    @PostMapping
    public void create(@Validated @RequestBody CreateOrderRequest createOrderRequest) {

        OrderCreatedEvent event = new OrderCreatedEvent(
                createOrderRequest.orderId(),
                createOrderRequest.customerId(),
                createOrderRequest.totalAmount(),
                createOrderRequest.currency(),
                Instant.now()
        );

        orderEventProducer.publishCreated(event);
    }

    @PostMapping("/cancel/{orderId}")
    public void cancel(@PathVariable String orderId, @Validated @RequestBody CancelOrderRequest cancelOrderRequest) {

        OrderCancelledEvent event = new OrderCancelledEvent(
          orderId,
          cancelOrderRequest.reason(),
          Instant.now()
        );

        orderEventProducer.publishCancelled(event);
    }
}
