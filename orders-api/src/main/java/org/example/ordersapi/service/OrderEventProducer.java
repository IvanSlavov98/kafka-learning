package org.example.ordersapi.service;

import org.example.ordersapi.model.events.OrderCancelledEvent;
import org.example.ordersapi.model.events.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> createdTemplate;
    private final KafkaTemplate<String, OrderCancelledEvent> cancelledTemplate;
    private final String createdTopic;
    private final String cancelledTopic;

    public OrderEventProducer(KafkaTemplate<String, OrderCreatedEvent> createdTemplate,
                              KafkaTemplate<String, OrderCancelledEvent> cancelledTemplate,
                              @Value("${topic.orders.created}") String createdTopic,
                              @Value("${topic.orders.cancelled}") String cancelledTopic) {
        this.createdTemplate = createdTemplate;
        this.cancelledTemplate = cancelledTemplate;
        this.createdTopic = createdTopic;
        this.cancelledTopic = cancelledTopic;
    }

    public CompletableFuture<SendResult<String, OrderCreatedEvent>> publishCreated(OrderCreatedEvent event) {
        return createdTemplate.send(createdTopic, event.orderId(), event);
    }

    public CompletableFuture<SendResult<String, OrderCancelledEvent>> publishCancelled(OrderCancelledEvent event) {
        return cancelledTemplate.send(cancelledTopic, event.orderId(), event);
    }

}
