package com.sourcing.sourcing.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.event.UserCreatedEvent;
import com.sourcing.sourcing.event.UserUpdatedEvent;
import com.sourcing.sourcing.user.producer.EventProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final EventProducer eventProducer;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public void createUser(String userId, String username) {
        UserCreatedEvent event = new UserCreatedEvent(userId, username);
        try {
            String message = objectMapper.writeValueAsString(event);
            handleEvent(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(String userId, String newUsername) {
        UserUpdatedEvent event = new UserUpdatedEvent(userId, newUsername);
        try {
            String message = objectMapper.writeValueAsString(event);
            handleEvent(message);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleEvent(String message) {
        eventProducer.produceEvent(message);
    }

    public UserDocument getUser(String userId) {
        return userRepository.findFirstByUserId(userId).orElseThrow(RuntimeException::new);
    }
}
