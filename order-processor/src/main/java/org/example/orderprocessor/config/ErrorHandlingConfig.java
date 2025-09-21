package org.example.orderprocessor.config;

import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
public class ErrorHandlingConfig {

    @Value("${topic.dlt.suffix:.DLT}")
    private String dltSuffix;

    @Bean
    public DeadLetterPublishingRecoverer dltRecoverer(KafkaTemplate<Object, Object> template) {
        return new DeadLetterPublishingRecoverer(template, (cr, e) -> {
            String dltTopic = cr.topic() + dltSuffix;
            return new TopicPartition(dltTopic, cr.partition());
        });
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
        var backoff = new ExponentialBackOffWithMaxRetries(3);
        backoff.setInitialInterval(500L);
        backoff.setMultiplier(2.0);
        backoff.setMaxInterval(5_000L);

        return new DefaultErrorHandler(recoverer, backoff);
    }
}
