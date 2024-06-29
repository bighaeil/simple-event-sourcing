package com.sourcing.sourcing.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sourcing.sourcing.event.Event;
import lombok.Getter;

public class UserUpdatedEvent implements Event {
    private final String userId;
    @Getter
    private final String newUsername;

    @JsonCreator
    public UserUpdatedEvent(@JsonProperty("userId") String userId, @JsonProperty("newUsername") String newUsername) {
        this.userId = userId;
        this.newUsername = newUsername;
    }

    @Override
    public String getType() {
        return "UserUpdatedEvent";
    }

    @Override
    public String getUserId() {
        return userId;
    }
}
