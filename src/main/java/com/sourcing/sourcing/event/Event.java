package com.sourcing.sourcing.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = UserCreatedEvent.class, name = "UserCreatedEvent"),
        @JsonSubTypes.Type(value = UserUpdatedEvent.class, name = "UserUpdatedEvent")
})
public interface Event {
    String getType();
    String getUserId();
    long getVersion();
}
