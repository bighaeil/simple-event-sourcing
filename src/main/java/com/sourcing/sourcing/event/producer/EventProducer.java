package com.sourcing.sourcing.event.producer;

import com.sourcing.sourcing.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventProducer {
    private final KafkaTemplate<String, Event> kafkaTemplate;

    public void produceEvent(Event event) {
        kafkaTemplate.send("user-events", event.getUserId(), event);
    }
}
