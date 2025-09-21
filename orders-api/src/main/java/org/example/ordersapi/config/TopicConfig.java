package org.example.ordersapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@Profile({"local"})
public class TopicConfig {

    @Bean
    public NewTopic ordersCreated(@Value("${topic.orders.created}") String name) {
        return TopicBuilder.name(name).partitions(3).replicas(1).build();
    }

    @Bean
    public NewTopic ordersCancelled(@Value("${topic.orders.cancelled}") String name) {
        return TopicBuilder.name(name).partitions(3).replicas(1).build();
    }

}
