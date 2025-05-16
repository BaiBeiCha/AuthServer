package com.baibei.authserver.service.kafka.producer;

import com.baibei.authserver.dto.UserAuthDto;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.kafka.enabled", havingValue = "true")
public class UserRegistrationProducer {

    @Value("${app.kafka.user-register-topic}")
    private String TOPIC;
    private final KafkaTemplate<String, UserAuthDto> kafkaTemplate;

    public void sendUserEvent(UserAuthDto user) {
        ProducerRecord<String, UserAuthDto> record = new ProducerRecord<>(TOPIC, user);
        record.headers().add("messageId", UUID.randomUUID().toString().getBytes());
        kafkaTemplate.send(record);
    }
}
