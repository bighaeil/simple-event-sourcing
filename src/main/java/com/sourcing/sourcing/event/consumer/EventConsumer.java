package com.sourcing.sourcing.event.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.event.repository.EventDocument;
import com.sourcing.sourcing.event.repository.EventRepository;
import com.sourcing.sourcing.event.Event;
import com.sourcing.sourcing.snapshot.Snapshot;
import com.sourcing.sourcing.snapshot.SnapshotRepository;
import com.sourcing.sourcing.user.UserAggregate;
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
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "events", groupId = "event-group")
    public void consume(Event event) {
        try {
            // 이벤트 저장
            saveEvent(event);

            // 스냅샷 및 오래된 이벤트 삭제 처리
            UserAggregate userAggregate = getUserAggregate(event.getUserId());
            userAggregate.apply(event);

            if (userAggregate.getVersion() % 10 == 0) {
                Snapshot snapshot = userAggregate.createSnapshot();
                snapshotRepository.save(snapshot);
                deleteOldEvents(event.getUserId(), userAggregate.getVersion());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void saveEvent(Event event) {
        EventDocument eventDocument = new EventDocument();
        eventDocument.setUserId(event.getUserId());
        eventDocument.setType(event.getType());
        eventDocument.setVersion(event.getVersion());
        try {
            eventDocument.setData(objectMapper.writeValueAsString(event));
        } catch (IOException e) {
            throw new RuntimeException("Failed to serialize event", e);
        }
        eventRepository.save(eventDocument);
    }

    private void deleteOldEvents(String userId, long version) {
        List<EventDocument> oldEvents = eventRepository.findByUserId(userId);
        for (EventDocument eventDoc : oldEvents) {
            if (eventDoc.getVersion() <= version - 10) {
                System.out.println("Deleting event with version: " + eventDoc.getVersion());
                eventRepository.delete(eventDoc);
            }
        }
    }

    private UserAggregate getUserAggregate(String userId) {
        Snapshot latestSnapshot = snapshotRepository.findFirstByUserIdOrderByVersionDesc(userId);
        UserAggregate userAggregate = new UserAggregate();
        if (latestSnapshot != null) {
            userAggregate.restoreFromSnapshot(latestSnapshot);
        }

        List<EventDocument> events = eventRepository.findByUserId(userId);
        for (EventDocument eventDoc : events) {
            Event event = deserializeEvent(eventDoc);
            if (event.getVersion() > userAggregate.getVersion()) {
                userAggregate.apply(event);
            }
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
