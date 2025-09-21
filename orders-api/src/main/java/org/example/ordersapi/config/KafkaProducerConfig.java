package org.example.ordersapi.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.example.ordersapi.model.events.OrderCancelledEvent;
import org.example.ordersapi.model.events.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    private Map<String, Object> baseProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, OrderCreatedEvent> pfOrderCreated() {
        return new DefaultKafkaProducerFactory<>(baseProps());
    }

    @Bean
    public KafkaTemplate<String, OrderCreatedEvent> ktOrderCreated() {
        return new KafkaTemplate<>(pfOrderCreated());
    }

    @Bean
    public ProducerFactory<String, OrderCancelledEvent> pfOrderCancelled() {
        return new DefaultKafkaProducerFactory<>(baseProps());
    }


    @Bean
    public KafkaTemplate<String, OrderCancelledEvent> ktOrderCancelled() {
        return new KafkaTemplate<>(pfOrderCancelled());
    }

}
