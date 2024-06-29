package com.sourcing.sourcing.user;

import com.sourcing.sourcing.event.Event;
import com.sourcing.sourcing.event.UserCreatedEvent;
import com.sourcing.sourcing.event.UserUpdatedEvent;
import com.sourcing.sourcing.snapshot.Snapshot;
import lombok.Getter;

@Getter
public class UserAggregate {
    private String userId;
    private String username;
    private long version = 0;

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
        this.version = event.getVersion(); // 이벤트의 버전을 적용합니다.
    }

    public Snapshot createSnapshot() {
        return new Snapshot(userId, username, version);
    }

    public void restoreFromSnapshot(Snapshot snapshot) {
        this.userId = snapshot.getUserId();
        this.username = snapshot.getUsername();
        this.version = snapshot.getVersion();
    }
}
