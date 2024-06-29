package com.sourcing.sourcing.user;

public class UserService {
    private final EventStore eventStore;
    private final UserAggregate userAggregate;

    public UserService(EventStore eventStore) {
        this.eventStore = eventStore;
        this.userAggregate = new UserAggregate();
    }

    public void createUser(String userId, String username) {
        UserCreatedEvent event = new UserCreatedEvent(userId, username);
        eventStore.save(event);
        userAggregate.apply(event);
    }

    public UserAggregate getUserAggregate() {
        userAggregate.loadFromHistory(eventStore.getEvents());
        return userAggregate;
    }
}
