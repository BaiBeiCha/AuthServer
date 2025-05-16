package com.baibei.authserver.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class KafkaConfig {

    @Value("${app.kafka.user-register-topic}")
    private String TOPIC;

    @Bean
    public NewTopic authTopic() {
        return new NewTopic(TOPIC, 1, (short) 1);
    }
}
