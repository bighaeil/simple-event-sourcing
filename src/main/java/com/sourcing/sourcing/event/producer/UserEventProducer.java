package com.sourcing.sourcing.event.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceEvent(String message) {
        kafkaTemplate.send("user_events", message);
    }
}
