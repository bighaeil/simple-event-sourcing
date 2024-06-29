package com.sourcing.sourcing.snapshot;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "snapshots")
public class Snapshot {
    @Id
    private String id;
    private String userId;
    private String username;
    private long version;

    public Snapshot(String userId, String username, long version) {
        this.userId = userId;
        this.username = username;
        this.version = version;
    }
}
