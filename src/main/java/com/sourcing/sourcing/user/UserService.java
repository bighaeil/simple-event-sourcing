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

    public void createUser(String userId, String username) {
        long version = getNextVersion(userId);
        UserCreatedEvent event = new UserCreatedEvent(userId, username, version);
        handleEvent(event);
    }

    public void updateUser(String userId, String newUsername) {
        long version = getNextVersion(userId);
        UserUpdatedEvent event = new UserUpdatedEvent(userId, newUsername, version);
        handleEvent(event);
    }

    private long getNextVersion(String userId) {
        List<EventDocument> events = eventRepository.findByUserId(userId);
        if (events.isEmpty()) {
            return 1;
        } else {
            return events.get(events.size() - 1).getVersion() + 1;
        }
    }

    private void handleEvent(Event event) {
        eventProducer.produceEvent(event);
    }

    public UserAggregate getUserAggregate(String userId) {
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
