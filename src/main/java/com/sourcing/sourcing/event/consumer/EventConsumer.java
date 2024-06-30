package com.sourcing.sourcing.event.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.event.Event;
import com.sourcing.sourcing.event.UserAggregate;
import com.sourcing.sourcing.event.producer.UserEventProducer;
import com.sourcing.sourcing.event.repository.EventDocument;
import com.sourcing.sourcing.event.repository.EventRepository;
import com.sourcing.sourcing.snapshot.Snapshot;
import com.sourcing.sourcing.snapshot.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventConsumer {
    private final EventRepository eventRepository;
    private final SnapshotRepository snapshotRepository;
    private final UserEventProducer eventProducer;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "events", groupId = "event-group")
    public void consume(String message) {
        // 이벤트 처리 로직
        try {
            Event event = objectMapper.readValue(message, Event.class);

            // 이벤트 저장
            saveEvent(event);

            UserAggregate userAggregate = getUserAggregate(event.getUserId());
            userAggregate.apply(event);

            // 스냅샷 생성 여부 판단 및 생성
            if (shouldCreateSnapshot(event.getUserId())) {
                createSnapshot(userAggregate);

                // 스냅샷 및 오래된 이벤트 삭제 처리
                deleteOldEvents(event.getUserId());
            }

            String s = objectMapper.writeValueAsString(userAggregate);

            eventProducer.produceEvent(s);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean shouldCreateSnapshot(String userId) {
        long count = eventRepository.countByUserId(userId);
        return count >= 10;
    }

    private void createSnapshot(UserAggregate userAggregate) {
        Snapshot snapshot = userAggregate.createSnapshot();
        snapshotRepository.save(snapshot);
    }

    private void saveEvent(Event event) {
        EventDocument eventDocument = new EventDocument();
        eventDocument.setUserId(event.getUserId());
        eventDocument.setType(event.getType());
        try {
            eventDocument.setData(objectMapper.writeValueAsString(event));
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
        eventRepository.save(eventDocument);
    }

    private void deleteOldEvents(String userId) {
        List<EventDocument> oldEvents = eventRepository.findByUserId(userId);
        for (EventDocument eventDoc : oldEvents) {
            System.out.println("Deleting event with version: " + eventDoc.getId());
            eventRepository.delete(eventDoc);
        }
    }

    public UserAggregate getUserAggregate(String userId) {
        Snapshot latestSnapshot = snapshotRepository.findFirstByUserIdOrderByIdDesc(userId);
        UserAggregate userAggregate = new UserAggregate();
        if (latestSnapshot != null) {
            userAggregate.restoreFromSnapshot(latestSnapshot);
        }

        List<EventDocument> events = eventRepository.findByUserId(userId);
        for (EventDocument eventDoc : events) {
            Event event = deserializeEvent(eventDoc);
            userAggregate.apply(event);
        }
        return userAggregate;
    }

    private Event deserializeEvent(EventDocument eventDoc) {
        try {
            return objectMapper.readValue(eventDoc.getData(), Event.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to deserialize event", e);
        }
    }
}
