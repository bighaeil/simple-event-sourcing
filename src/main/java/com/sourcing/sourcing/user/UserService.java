package com.sourcing.sourcing.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.event.Event;
import com.sourcing.sourcing.event.UserCreatedEvent;
import com.sourcing.sourcing.event.UserUpdatedEvent;
import com.sourcing.sourcing.event.producer.EventProducer;
import com.sourcing.sourcing.event.repository.EventDocument;
import com.sourcing.sourcing.event.repository.EventRepository;
import com.sourcing.sourcing.snapshot.Snapshot;
import com.sourcing.sourcing.snapshot.SnapshotRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final EventProducer eventProducer;
    private final EventRepository eventRepository;
    private final SnapshotRepository snapshotRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public void createUser(String userId, String username) {
        UserCreatedEvent event = new UserCreatedEvent(userId, username);
        handleEvent(event);
    }

    @Transactional
    public void updateUser(String userId, String newUsername) {
        UserUpdatedEvent event = new UserUpdatedEvent(userId, newUsername);
        handleEvent(event);
    }

    private void handleEvent(Event event) {
        eventProducer.produceEvent(event);
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
