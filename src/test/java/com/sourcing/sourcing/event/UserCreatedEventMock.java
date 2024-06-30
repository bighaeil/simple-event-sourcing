package com.sourcing.sourcing.event;

public class UserCreatedEventMock extends UserCreatedEvent {
    private UserCreatedEventMock(String userId, String username) {
        super(userId, username);
    }

    public static UserCreatedEvent create(String userId, String username) {
        return new UserCreatedEventMock(userId, username);
    }
}
