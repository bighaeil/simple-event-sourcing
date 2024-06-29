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
        } else if (event instanceof UserUpdatedEvent) {
            UserUpdatedEvent userUpdatedEvent = (UserUpdatedEvent) event;
            if (this.userId.equals(userUpdatedEvent.getUserId())) {
                this.username = userUpdatedEvent.getNewUsername();
            }
        }
        // 다른 이벤트 타입에 대한 처리를 추가할 수 있습니다.
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
