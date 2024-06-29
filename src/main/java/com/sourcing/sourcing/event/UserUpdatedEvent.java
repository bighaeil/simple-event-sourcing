package com.sourcing.sourcing.event;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class UserUpdatedEvent implements Event {
    private final String userId;
    @Getter
    private final String newUsername;
    private final long version;

    @JsonCreator
    public UserUpdatedEvent(@JsonProperty("userId") String userId, @JsonProperty("newUsername") String newUsername, @JsonProperty("version") long version) {
        this.userId = userId;
        this.newUsername = newUsername;
        this.version = version;
    }

    @Override
    public String getType() {
        return "UserUpdatedEvent";
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
