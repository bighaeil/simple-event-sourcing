package com.sourcing.sourcing.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UserCreatedEvent implements Event {
    private final String userId;
    @Getter
    private final String username;
    private final long version;
    @JsonCreator
    public UserCreatedEvent(@JsonProperty("userId") String userId, @JsonProperty("username") String username, @JsonProperty("version") long version) {
        this.userId = userId;
        this.username = username;
        this.version = version;
    }

    @Override
    public String getType() {
        return "UserCreatedEvent";
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public long getVersion() {
        return version;
    }
}

