package com.sourcing.sourcing.snapshot;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface SnapshotRepository extends MongoRepository<Snapshot, String> {
    Snapshot findFirstByUserIdOrderByVersionDesc(String userId);
}
