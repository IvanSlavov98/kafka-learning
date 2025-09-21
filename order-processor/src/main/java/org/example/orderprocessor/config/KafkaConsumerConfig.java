package org.example.orderprocessor.config;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.example.orderprocessor.model.OrderCancelledEvent;
import org.example.orderprocessor.model.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

    private final String bootstrap;
    private final String groupId;
    private final int concurrency;

    public KafkaConsumerConfig(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrap,
            @Value("${app.group-id}") String groupId,
            @Value("${app.concurrency}") int concurrency) {
        this.bootstrap = bootstrap;
        this.groupId = groupId;
        this.concurrency = concurrency;
    }

    private Map<String, Object> baseProps() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrap);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        return props;
    }

    @Bean
    public ConsumerFactory<String, OrderCreatedEvent> cfCreated() {
        var deserializer = new JsonDeserializer<>(OrderCreatedEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(baseProps(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConsumerFactory<String, OrderCancelledEvent> cfCancelled() {
        var deserializer = new JsonDeserializer<>(OrderCancelledEvent.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeHeaders(false);
        return new DefaultKafkaConsumerFactory<>(baseProps(), new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent> lfCreated(
            org.springframework.kafka.listener.CommonErrorHandler errorHandler) {
        var f = new ConcurrentKafkaListenerContainerFactory<String, OrderCreatedEvent>();
        f.setConsumerFactory(cfCreated());
        f.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        f.setCommonErrorHandler(errorHandler);
        f.setConcurrency(concurrency);
        return f;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent> lfCancelled(
            org.springframework.kafka.listener.CommonErrorHandler errorHandler) {
        var f = new ConcurrentKafkaListenerContainerFactory<String, OrderCancelledEvent>();
        f.setConsumerFactory(cfCancelled());
        f.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        f.setCommonErrorHandler(errorHandler);
        f.setConcurrency(concurrency);
        return f;
    }
}
