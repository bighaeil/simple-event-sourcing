package com.sourcing.sourcing.user;

import java.util.ArrayList;
import java.util.List;

public class EventStore {
    private final List<Event> events = new ArrayList<>();

    public void save(Event event) {
        events.add(event);
    }

    public List<Event> getEvents() {
        return new ArrayList<>(events);
    }
}
