package com.sourcing.sourcing.event.repository;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "userEvents")
public class EventDocument {
    @Id
    private String id;
    private String userId;
    private String type;
    private String data;
}
