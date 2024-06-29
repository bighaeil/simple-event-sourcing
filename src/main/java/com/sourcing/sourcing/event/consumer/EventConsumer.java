package com.sourcing.sourcing.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.event.repository.EventDocument;
import com.sourcing.sourcing.event.repository.EventRepository;
import com.sourcing.sourcing.event.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventConsumer {
    private final EventRepository eventRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "events", groupId = "event-group")
    public void consume(Event event) {
        try {
            EventDocument document = new EventDocument();
            document.setUserId(event.getUserId());
            document.setType(event.getType());
            document.setData(objectMapper.writeValueAsString(event));

            eventRepository.save(document);
        } catch (Exception e) {
            // 에러 처리
        }
    }
}
