package com.sourcing.sourcing.user;

public class UserCreatedEvent implements Event {
    private final String userId;
    private final String username;

    public UserCreatedEvent(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public String getType() {
        return "UserCreatedEvent";
    }
}

