package com.sourcing.sourcing.user;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Test
    public void EXAMPLE_TEST() {
        EventStore eventStore = new EventStore();
        UserService userService = new UserService(eventStore);

        userService.createUser("1", "JohnDoe");

        UserAggregate userAggregate = userService.getUserAggregate();

        assertEquals("JohnDoe", userAggregate.getUsername());
    }
}