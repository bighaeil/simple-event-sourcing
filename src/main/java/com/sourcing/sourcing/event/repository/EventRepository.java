package com.sourcing.sourcing.event.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<EventDocument, String> {
    List<EventDocument> findByUserId(String userId);
}
