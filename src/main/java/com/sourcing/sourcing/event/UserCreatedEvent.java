package com.sourcing.sourcing.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UserCreatedEvent implements Event {
    private final String userId;
    @Getter
    private final String username;

    @JsonCreator
    public UserCreatedEvent(@JsonProperty("userId") String userId, @JsonProperty("username") String username) {
        this.userId = userId;
        this.username = username;
    }

    @Override
    public String getType() {
        return "UserCreatedEvent";
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
