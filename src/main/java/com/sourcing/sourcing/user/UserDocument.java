package com.sourcing.sourcing.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document("users")
public class UserDocument {
    @Id
    private String id;
    private String userId;
    private String username;

    public UserDocument(String userId, String username) {
        this.userId = userId;
        this.username = username;
    }
}
