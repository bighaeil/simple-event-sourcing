package com.sourcing.sourcing.event;

import com.sourcing.sourcing.snapshot.Snapshot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAggregate {
    private String userId;
    private String username;

    public void apply(Event event) {
        if (event instanceof UserCreatedEvent userCreatedEvent) {
            this.userId = userCreatedEvent.getUserId();
            this.username = userCreatedEvent.getUsername();
        } else if (event instanceof UserUpdatedEvent userUpdatedEvent) {
            if (this.userId.equals(userUpdatedEvent.getUserId())) {
                this.username = userUpdatedEvent.getNewUsername();
            }
        }
    }

    public Snapshot createSnapshot() {
        return new Snapshot(userId, username);
    }

    public void restoreFromSnapshot(Snapshot snapshot) {
        this.userId = snapshot.getUserId();
        this.username = snapshot.getUsername();
    }
}
