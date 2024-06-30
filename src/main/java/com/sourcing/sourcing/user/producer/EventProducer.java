package com.sourcing.sourcing.user.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceEvent(String message) {
        kafkaTemplate.send("events", message);
    }
}
