package com.sourcing.sourcing.snapshot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "userSnapshots")
public class Snapshot {
    @Id
    private String id;
    private String userId;
    private String username;

    public Snapshot(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
