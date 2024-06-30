package com.sourcing.sourcing.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sourcing.sourcing.ConcurrencyTest;
import com.sourcing.sourcing.event.producer.EventProducer;
import com.sourcing.sourcing.event.repository.EventRepository;
import com.sourcing.sourcing.snapshot.SnapshotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    private EventProducer eventProducer;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private SnapshotRepository snapshotRepository;
    @Mock
    private ObjectMapper objectMapper;

    @Test
    public void EXAMPLE_TEST() {
        //given
        String userId = "1";
        String username = "test";

        //when
        userService.createUser(userId, username);

        //then

    }
}