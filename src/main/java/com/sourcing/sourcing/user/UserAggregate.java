package com.sourcing.sourcing.user;

import com.sourcing.sourcing.event.Event;

import java.util.List;

public class UserAggregate {
    private String userId;
    private String username;

    public void apply(Event event) {
        if (event instanceof UserCreatedEvent) {
            UserCreatedEvent userCreatedEvent = (UserCreatedEvent) event;
            this.userId = userCreatedEvent.getUserId();
            this.username = userCreatedEvent.getUsername();
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
