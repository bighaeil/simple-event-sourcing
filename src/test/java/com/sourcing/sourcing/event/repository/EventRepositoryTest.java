package com.sourcing.sourcing.event.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void SEARCH_TEST() {
        // given
        Query query = new Query();

        // when
        eventRepository.findAll();

        // then
    }

}