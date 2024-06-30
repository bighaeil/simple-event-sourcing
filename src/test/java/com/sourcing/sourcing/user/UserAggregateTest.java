package com.sourcing.sourcing.user;

import com.sourcing.sourcing.event.UserAggregate;
import com.sourcing.sourcing.event.UserCreatedEvent;
import com.sourcing.sourcing.event.UserCreatedEventMock;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserAggregateTest {

    @Test
    void APPLY_TEST() {
        //given
        String userId = "1";
        String username = "test";
        UserAggregate userAggregate = UserAggregateMock.create(userId, username);

        //when
        UserCreatedEvent event = UserCreatedEventMock.create(userId, username);
        userAggregate.apply(event);

        //then
        assertEquals(username, userAggregate.getUsername());
    }

    @Test
    void MANY_APPLY_TEST() {
        //given
        String userId = "1";
        String username = "test";
        String username2 = "test2";
        UserAggregate userAggregate = UserAggregateMock.create(userId, username);

        //when
        UserCreatedEvent event = UserCreatedEventMock.create(userId, username);
        userAggregate.apply(event);
        UserCreatedEvent event2 = UserCreatedEventMock.create(userId, username2);
        userAggregate.apply(event2);

        //then
        assertEquals(username2, userAggregate.getUsername());
    }
}