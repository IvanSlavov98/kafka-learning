package org.example.orderprocessor.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.example.orderprocessor.model.OrderCancelledEvent;
import org.example.orderprocessor.model.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderEventsListener {


    @Value("${topic.orders.created}")
    private String createdTopic;
    @Value("${topic.orders.cancelled}")
    private String cancelledTopic;
    private final Logger logger = LoggerFactory.getLogger(OrderEventsListener.class);

    @KafkaListener(
            topics = "#{@environment.getProperty('topic.orders.created')}",
            containerFactory = "lfCreated"
    )
    public void onCreated(@Payload OrderCreatedEvent event,
                          ConsumerRecord<String, OrderCreatedEvent> record,
                          Acknowledgment ack) throws Exception {

        if (event.totalAmount().compareTo(BigDecimal.ZERO) < 0) {
            throw new Exception("total amount is negative");
        }

        logger.info("Received OrderCreatedEvent with id: {}, customer: {}, amount: {}, currency: {}, createdAt: {}",
                event.orderId(),
                event.customerId(),
                event.totalAmount(),
                event.currency(),
                event.createdAt());

        ack.acknowledge();

    }

    @KafkaListener(
            topics = "#{@environment.getProperty('topic.orders.cancelled')}",
            containerFactory = "lfCancelled"
    )
    public void onCancelled(@Payload OrderCancelledEvent event,
                            ConsumerRecord<String, OrderCancelledEvent> record,
                            Acknowledgment ack) {
        logger.info("Received OrderCancelledEvent with id: {}, reason: {}, cancelledAt: {}",
                event.orderId(),
                event.reason(),
                event.cancelledAt());
        ack.acknowledge();

    }
}
