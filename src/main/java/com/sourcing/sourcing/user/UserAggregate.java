package com.sourcing.sourcing.user;

import java.util.List;

public class UserAggregate {
    private String userId;
    private String username;

    public UserAggregate() {
    }

    public void apply(Event event) {
        if (event instanceof UserCreatedEvent) {
            UserCreatedEvent userCreatedEvent = (UserCreatedEvent) event;
            this.userId = userCreatedEvent.getUserId();
            this.username = userCreatedEvent.getUsername();
        }
        // 다른 이벤트 타입에 대한 처리를 추가할 수 있습니다.
    }

    public void loadFromHistory(List<Event> history) {
        for (Event event : history) {
            apply(event);
        }
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }
}
