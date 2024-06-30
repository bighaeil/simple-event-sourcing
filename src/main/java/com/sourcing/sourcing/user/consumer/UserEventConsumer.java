package com.sourcing.sourcing.user.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.event.UserAggregate;
import com.sourcing.sourcing.user.UserDocument;
import com.sourcing.sourcing.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserEventConsumer {
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "user_events", groupId = "event-group")
    public void consume(String message) {
        // 이벤트 처리 로직
        try {
            UserAggregate userAggregate = objectMapper.readValue(message, UserAggregate.class);

            UserDocument userDocument = userRepository.findFirstByUserId(userAggregate.getUserId())
                    .map(document -> {
                        document.setUsername(userAggregate.getUsername());

                        return document;
                    })
                    .orElse(new UserDocument(userAggregate.getUserId(), userAggregate.getUsername()));

            userRepository.save(userDocument);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
