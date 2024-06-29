package com.sourcing.sourcing.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.event.Event;
import com.sourcing.sourcing.event.producer.EventProducer;
import com.sourcing.sourcing.event.repository.EventDocument;
import com.sourcing.sourcing.event.repository.EventRepository;
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
    private final ObjectMapper objectMapper;

    public void createUser(String userId, String username) {
        UserCreatedEvent event = new UserCreatedEvent(userId, username);
        eventProducer.produceEvent(event);
    }

    public void updateUser(String userId, String newUsername) {
        UserUpdatedEvent event = new UserUpdatedEvent(userId, newUsername);
        eventProducer.produceEvent(event);
    }

    public UserAggregate getUserAggregate(String userId) {
        List<EventDocument> events = eventRepository.findByUserId(userId);
        UserAggregate userAggregate = new UserAggregate();

        for (EventDocument eventDocument: events) {
            Event event = deserializeEvent(eventDocument);
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
